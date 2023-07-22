package domain;

import java.util.List;

public class Account {
    private final String uuid;
    private final Owner owner;
    private final List<Movement> movements;

    public Account(String uuid, Owner owner, List<Movement> movements) {
        this.uuid = uuid;
        this.owner = owner;
        this.movements = movements;
    }

    public void addMovement(Double quantity, MovementType type) {
        this.movements.add(new Movement(quantity, type));
    }

    public String getUuid() {
        return uuid;
    }

    public Owner getOwner() {
        return owner;
    }

    public Double getBalance() {
        return movements.stream()
                .mapToDouble(movement -> movement.getType() == MovementType.INCOME ? movement.getQuantity() : -movement.getQuantity())
                .sum();
    }

    public List<Movement> getMovements() {
        return movements;
    }

    @Override
    public String toString() {
        return "Account{" +
                "uuid='" + uuid + '\'' +
                ", owner=" + owner +
                ", balance=" + getBalance() +
                ", movements=" + movements +
                '}';
    }
}
