package domain;

public class Movement {
    private final Double quantity;
    private final MovementType type;

    public Movement(Double quantity, MovementType type) {
        this.quantity = quantity;
        this.type = type;
    }

    public Double getQuantity() {
        return quantity;
    }

    public MovementType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Movement{" +
                "quantity=" + quantity +
                ", type=" + type +
                '}';
    }
}
