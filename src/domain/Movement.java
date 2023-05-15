package domain;

public class Movement {
    private final MovementType type;

    public Movement(MovementType type) {
        this.type = type;
    }

    public MovementType getType() {
        return type;
    }
}
