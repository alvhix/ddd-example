package main.account.domain;

import java.util.List;
import java.util.Optional;

public class Account {
    private final String uuid;
    private final Owner owner;
    private Double balance;
    private final List<Movement> movements;

    public Account(String uuid, Owner owner, List<Movement> movements) {
        this.uuid = uuid;
        this.owner = owner;
        this.movements = movements;
        this.balance = this.getBalance();
    }

    public void addMovement(Double amount, MovementType type) {
        this.movements.add(new Movement(amount, type));
        this.balance = this.getBalance();
    }

    public String getUuid() {
        return this.uuid;
    }

    public Double getBalance() {

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
