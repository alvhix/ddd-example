package account.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class MovementMother {
    private static Movement createSingleMovement() {
        MovementType[] types = MovementType.values();
        int randomIndex = (int) (Math.random() * types.length);
        long amount = Math.round(Math.random() * 1000);

        return new Movement(UUID.randomUUID(), (double) amount, types[randomIndex]);
    }

    public static Set<Movement> create() {
        int numberOfMovements = (int) (Math.random() * 100) + 1;

        Set<Movement> movements = new HashSet<>();
        for (int i = 0; i < numberOfMovements; i++) {
            movements.add(createSingleMovement());
        }
        return movements;
    }
}
