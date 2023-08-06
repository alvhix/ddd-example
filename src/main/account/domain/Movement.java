package main.account.domain;

import java.util.UUID;

public class Movement {
    private final UUID uuid;
    private final Double amount;
    private final MovementType type;

    public Movement(UUID uuid, Double amount, MovementType type) {
        this.uuid = uuid;
        this.amount = amount;
        this.type = type;
    }

    public static Movement create(Double amount, MovementType type) {
        return new Movement(UUID.randomUUID(), amount, type);
    }

    public UUID uuid() {
        return uuid;
    }

    public Double amount() {
        return amount;
    }

    public MovementType type() {
        return type;
    }
}
