package main.account.domain;

import main.shared.domain.DomainEvent;

public class MovementCreated implements DomainEvent {
    private static final String FULL_QUALIFIED_EVENT_NAME = "movement-created";
    private final String accountUuid;
    private final MovementType movementType;
    private final Double amount;

    public MovementCreated(String accountUuid, Double amount, MovementType movementType) {
        this.accountUuid = accountUuid;
        this.amount = amount;
        this.movementType = movementType;
    }

    @Override
    public String fullQualifiedEventName() {
        return FULL_QUALIFIED_EVENT_NAME;
    }

    @Override
    public String toString() {
        return "MovementCreated{" +
                "accountUuid='" + accountUuid + '\'' +
                ", movementType=" + movementType +
                ", amount=" + amount +
                '}';
    }
}
