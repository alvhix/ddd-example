package shared.infrastructure.persistence;

import account.domain.Account;
import account.domain.Movement;
import account.domain.MovementType;
import account.domain.Owner;
import account.infrastructure.persistence.entity.AccountEntity;
import account.infrastructure.persistence.entity.MovementEntity;
import account.infrastructure.persistence.entity.OwnerEntity;

import java.util.Set;
import java.util.stream.Collectors;

public final class Mapper {
    public static Account mapToDomain(AccountEntity accountEntity) {
        return new Account(
                accountEntity.uuid(),
                mapToDomain(accountEntity.owner()),
                mapToDomain(accountEntity.movements())
        );
    }

    public static Set<Movement> mapToDomain(Set<MovementEntity> movements) {
        return movements
                .stream()
                .map(movement -> new Movement(movement.uuid(), movement.amount(), MovementType.valueOf(movement.type())))
                .collect(Collectors.toSet());
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

    public static Set<MovementEntity> mapToEntity(Set<Movement> movements) {
        return movements.stream()
                .map(x -> new MovementEntity(x.uuid(), x.amount(), x.type().toString()))
                .collect(Collectors.toSet());
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
