package test;

import main.account.application.AccountSearcher;
import main.account.application.TransferenceService;
import main.account.domain.Account;
import main.account.domain.Movement;
import main.account.domain.MovementType;
import main.account.domain.Owner;
import main.account.infrastructure.persistence.InMemoryRepositoryImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public final class AccountServiceShould {

    private InMemoryRepositoryImpl inMemoryRepository;

    @Test
    public void testGetAllAccounts() {
        // arrange
        List<Account> accounts = List.of(
                new Account(null, null, null),
                new Account(null, null, null),
                new Account(null, null, null),
                new Account(null, null, null)
        );
        inMemoryRepository = new InMemoryRepositoryImpl(accounts);
        AccountSearcher accountSearcher = new AccountSearcher(inMemoryRepository);

        // act
        List<Account> accountsResult = accountSearcher.getAllAccounts();

        // assert
        assertEquals(4, accountsResult.size());
    }

    @Test
    public void testGetAllMovements() {
        // arrange
        String uuid = "249c9b83-4912-4719-9d5d-a27a3b4c4a8c";
        List<Account> accounts = List.of(
                new Account(uuid,
                        new Owner("Maria", "Garcia", "22392403V"),
                        new ArrayList<>(List.of(
                                new Movement(1000.00, MovementType.INCOME),
                                new Movement(500.00, MovementType.EXPENSE))
                        )
                )
        );

        inMemoryRepository = new InMemoryRepositoryImpl(accounts);
        AccountSearcher accountSearcher = new AccountSearcher(inMemoryRepository);

        // act
        List<Movement> movements = accountSearcher.getAllMovements(uuid);

        // assert
        assertEquals(2, movements.size());
    }

    @Test
    public void testTransfer() {
        // arrange
        String origin = "ff6f9c90-bbb7-409c-87d2-04277f85d111";
        String destination = "249c9b83-4912-4719-9d5d-a27a3b4c4a8c";
        List<Account> accounts = List.of(
                new Account(origin,
                        new Owner("William", "Mote", "43957942C"),
                        new ArrayList<>(List.of(
                                new Movement(1000.00, MovementType.INCOME),
                                new Movement(200.00, MovementType.INCOME))
                        )
                ),
                new Account(destination,
                        new Owner("Maria", "Garcia", "22392403V"),
                        new ArrayList<>(List.of(
                                new Movement(1000.00, MovementType.INCOME),
                                new Movement(500.00, MovementType.EXPENSE))
                        )
                )
        );

        inMemoryRepository = new InMemoryRepositoryImpl(accounts);
        TransferenceService transferenceService = new TransferenceService(inMemoryRepository);

        // act
        transferenceService.transfer(origin, destination, 200.00);

        // assert
        assertEquals(1000.00, accounts.get(0).getBalance());
        assertEquals(700.00, accounts.get(1).getBalance());
    }

    @Test
    public void testDeposit() {
        // arrange
        String uuid = "249c9b83-4912-4719-9d5d-a27a3b4c4a8c";
        List<Account> accounts = List.of(
                new Account(uuid,
                        new Owner("Maria", "Garcia", "22392403V"),
                        new ArrayList<>(List.of(
                                new Movement(1000.00, MovementType.INCOME),
                                new Movement(500.00, MovementType.EXPENSE))
                        )
                )
        );

        inMemoryRepository = new InMemoryRepositoryImpl(accounts);
        TransferenceService transferenceService = new TransferenceService(inMemoryRepository);

        // act
        Double balance = transferenceService.deposit(uuid, 200.00);

        // assert
        assertEquals(700.00, balance);
    }

    @Test
    public void testWithdraw() {
        // arrange
        String uuid = "249c9b83-4912-4719-9d5d-a27a3b4c4a8c";
        List<Account> accounts = List.of(
                new Account(uuid,
                        new Owner("Maria", "Garcia", "22392403V"),
                        new ArrayList<>(List.of(
                                new Movement(1000.00, MovementType.INCOME),
                                new Movement(500.00, MovementType.EXPENSE))
                        )
                )
        );

        inMemoryRepository = new InMemoryRepositoryImpl(accounts);
        TransferenceService transferenceService = new TransferenceService(inMemoryRepository);

        // act
        Double balance = transferenceService.withdraw(uuid, 200.00);

        // assert
        assertEquals(300.00, balance);
    }
}
