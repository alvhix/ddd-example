package account.domain;

import shared.domain.AggregateRoot;

import java.util.*;

public final class Account extends AggregateRoot {
    private final UUID uuid;
    private Double balance;
    private final Owner owner;
    private final Set<Movement> movements;

    public Account(UUID uuid, Owner owner, Set<Movement> movements) {
        this.uuid = uuid;
        this.owner = owner;
        this.movements = movements;
        this.balance = this.calculateBalance();
    }

    public static Account create(Owner owner, Set<Movement> movements) {
        return new Account(UUID.randomUUID(), owner, movements);
    }

    public void addMovement(Double amount, MovementType type) {
        this.movements.add(Movement.create(amount, type));
        this.balance = this.calculateBalance();
        super.record(new MovementCreated(this.uuid(), amount, type));
    }

    public UUID uuid() {
        return this.uuid;
    }

    public Double balance() {
        return balance;
    }

    public Owner owner() {
        return owner;
    }

    public Double calculateBalance() {

        return Optional.ofNullable(this.movements)
                .stream()
                .flatMap(Set::stream)
                .mapToDouble(movement -> movement.type() == MovementType.INCOME ? movement.amount() : -movement.amount())
                .sum();
    }

    public Set<Movement> movements() {
        return this.movements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return Objects.equals(uuid, account.uuid) && Objects.equals(balance, account.balance) && Objects.equals(owner, account.owner) && Objects.equals(movements, account.movements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, balance, owner, movements);
    }
}
