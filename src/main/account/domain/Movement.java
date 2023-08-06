package main.account.domain;

public class Movement {
    private final Double amount;
    private final MovementType type;

    public Movement(Double amount, MovementType type) {
        this.amount = amount;
        this.type = type;
    }

    public Double amount() {
        return amount;
    }

    public MovementType type() {
        return type;
    }
}
