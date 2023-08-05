package main.account.domain;

import main.shared.domain.AggregateRoot;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Account extends AggregateRoot {
    private final AccountUuid uuid;
    private Double balance;
    private final List<Movement> movements;

    public Account(AccountUuid uuid, Owner owner, List<Movement> movements) {
        this.uuid = uuid;
        this.movements = movements;
        this.balance = this.calculateBalance();
    }

    public static Account create(Owner owner, List<Movement> movements) {
        return new Account(new AccountUuid(UUID.randomUUID().toString()), owner, movements);
    }

    public void addMovement(Double amount, MovementType type) {
        this.movements.add(new Movement(amount, type));
        this.balance = this.calculateBalance();
        super.record(new MovementCreated(this.getUuid(), amount, type));
    }

    public String getUuid() {
        return this.uuid.value();
    }

    public Double getBalance() {
        return balance;
    }

    public Double calculateBalance() {

        return Optional.ofNullable(this.movements)
                .stream()
                .flatMap(List::stream)
                .mapToDouble(movement -> movement.getType() == MovementType.INCOME ? movement.getAmount() : -movement.getAmount())
                .sum();
    }

    public List<Movement> getMovements() {
        return this.movements;
    }
}
