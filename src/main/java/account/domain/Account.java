package account.domain;

import shared.domain.AggregateRoot;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Account extends AggregateRoot {
    private final UUID uuid;
    private Double balance;
    private final Owner owner;
    private final List<Movement> movements;

    public Account(UUID uuid, Owner owner, List<Movement> movements) {
        this.uuid = uuid;
        this.owner = owner;
        this.movements = movements;
        this.balance = this.calculateBalance();
    }

    public static Account create(Owner owner, List<Movement> movements) {
        return new Account(UUID.randomUUID(), owner, movements);
    }

    public void addMovement(Double amount, MovementType type) {
        this.movements.add(Movement.create(amount, type));
        this.balance = this.calculateBalance();
        super.record(new MovementCreated(this.uuid(), amount, type));
    }

    public Movement lastMovement() {
        return movements.get(movements.size() - 1);
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
                .flatMap(List::stream)
                .mapToDouble(movement -> movement.type() == MovementType.INCOME ? movement.amount() : -movement.amount())
                .sum();
    }

    public List<Movement> movements() {
        return this.movements;
    }
}
