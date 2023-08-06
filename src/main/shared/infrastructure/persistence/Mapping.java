package main.shared.infrastructure.persistence;

import main.account.domain.Account;
import main.account.domain.Movement;
import main.account.domain.MovementType;
import main.account.domain.Owner;
import main.account.infrastructure.persistence.vo.AccountVO;
import main.account.infrastructure.persistence.vo.MovementVO;
import main.account.infrastructure.persistence.vo.OwnerVO;

import java.util.List;
import java.util.stream.Collectors;

public class Mapping {

    public Account mapToDomain(AccountVO accountVO) {
        return new Account(accountVO.uuid(),
                new Owner(accountVO.owner().uuid(), accountVO.owner().name(), accountVO.owner().firstName(), accountVO.owner().nif()),
                this.mapMovementsToDomain(accountVO.movements()));
    }

    public Movement mapToDomain(MovementVO movementVO) {
        return new Movement(movementVO.uuid(), movementVO.amount(), MovementType.valueOf(movementVO.type()));
    }

    public AccountVO mapToPersistence(Account account) {
        return new AccountVO(account.uuid(),
                new OwnerVO(account.owner().uuid(), account.owner().name(), account.owner().firstName(), account.owner().nif()),
                this.mapMovementsToPersistence(account.movements()),
                account.balance());
    }

    public MovementVO mapToPersistence(Movement movement) {
        return new MovementVO(movement.uuid(), movement.amount(), movement.type().toString());
    }

    public List<Account> mapAccountsToDomain(List<AccountVO> accountVOList) {
        return accountVOList.stream().map(this::mapToDomain).collect(Collectors.toList());
    }

    public List<Movement> mapMovementsToDomain(List<MovementVO> movementVOList) {
        return movementVOList.stream().map(this::mapToDomain).collect(Collectors.toList());
    }

    public List<AccountVO> mapAccountsToPersistence(List<Account> accounts) {
        return accounts.stream().map(this::mapToPersistence).collect(Collectors.toList());
    }

    public List<MovementVO> mapMovementsToPersistence(List<Movement> movements) {
        return movements.stream().map(this::mapToPersistence).collect(Collectors.toList());
    }
}
