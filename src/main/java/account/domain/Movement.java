package account.domain;

import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movement)) return false;
        Movement movement = (Movement) o;
        return Objects.equals(uuid, movement.uuid) && Objects.equals(amount, movement.amount) && type == movement.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, amount, type);
    }
}
