package main.account.infrastructure.dto;

import java.util.UUID;

public final class MovementDto {
    public UUID origin;
    public UUID destination;
    public Double amount;
}