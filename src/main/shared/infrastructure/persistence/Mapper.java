package main.shared.infrastructure.persistence;

import main.account.domain.Account;
import main.account.domain.Movement;
import main.account.domain.MovementType;
import main.account.domain.Owner;
import main.account.infrastructure.persistence.mapper.entity.AccountEntity;
import main.account.infrastructure.persistence.mapper.entity.MovementEntity;
import main.account.infrastructure.persistence.mapper.entity.OwnerEntity;

import java.util.List;
import java.util.stream.Collectors;

public class Mapper {
    public static Account mapToDomain(AccountEntity accountEntity) {
        return new Account(
                accountEntity.uuid(),
                mapToDomain(accountEntity.owner()),
                mapToDomain(accountEntity.movements())
        );
    }

    public static List<Movement> mapToDomain(List<MovementEntity> movements) {
        return movements
                .stream()
                .map(movement -> new Movement(movement.uuid(), movement.amount(), MovementType.valueOf(movement.type())))
                .collect(Collectors.toList());
    }

    public static Owner mapToDomain(OwnerEntity ownerEntity) {
        return new Owner(
                ownerEntity.uuid(),
                ownerEntity.name(),
                ownerEntity.firstName(),
                ownerEntity.nif()
        );
    }

    public static AccountEntity mapToEntity(Account account) {

        return new AccountEntity(
                account.uuid(),
                account.balance()
        );
    }

    public static List<MovementEntity> mapToEntity(List<Movement> movements) {
        return movements.stream()
                .map(x -> new MovementEntity(x.uuid(), x.amount(), x.type().toString()))
                .collect(Collectors.toList());
    }

    public static OwnerEntity mapToEntity(Owner owner) {
        return new OwnerEntity(
                owner.uuid(),
                owner.name(),
                owner.firstName(),
                owner.nif()
        );
    }
}
