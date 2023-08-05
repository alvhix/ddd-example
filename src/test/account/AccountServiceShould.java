package test.account;

import main.account.application.AccountSearcher;
import main.account.application.MovementService;
import main.account.domain.*;
import main.account.infrastructure.persistence.InMemoryRepositoryImpl;
import org.junit.jupiter.api.Test;
import test.account.infrastructure.FakeEventBus;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class AccountServiceShould {

    private InMemoryRepositoryImpl inMemoryRepository;

    @Test
    public void testGetAllAccounts() {
        // arrange
        List<Account> accounts = new ArrayList<>(List.of(
                new Account(null, null, null),
                new Account(null, null, null),
                new Account(null, null, null),
                new Account(null, null, null)
        ));
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
        List<Account> accounts = new ArrayList<>(List.of(
                Account.create(new Owner("Maria", "Garcia", "22392403V"),
                        new ArrayList<>(List.of(
                                new Movement(1000.00, MovementType.INCOME),
                                new Movement(500.00, MovementType.EXPENSE))
                        )
                )
        ));

        inMemoryRepository = new InMemoryRepositoryImpl(accounts);
        AccountSearcher accountSearcher = new AccountSearcher(inMemoryRepository);

        // act
        List<Movement> movements;
        try {
            movements = accountSearcher.getAllMovements(accounts.get(0).getUuid());
        } catch (AccountNotFound e) {
            throw new RuntimeException(e);
        }

        // assert
        assertEquals(2, movements.size());
    }

    @Test
    public void testTransfer() {
        // arrange
        List<Account> accounts = new ArrayList<>(List.of(
                Account.create(new Owner("William", "Mote", "43957942C"),
                        new ArrayList<>(List.of(
                                new Movement(1000.00, MovementType.INCOME),
                                new Movement(200.00, MovementType.INCOME))
                        )
                ),
                Account.create(new Owner("Maria", "Garcia", "22392403V"),
                        new ArrayList<>(List.of(
                                new Movement(1000.00, MovementType.INCOME),
                                new Movement(500.00, MovementType.EXPENSE))
                        )
                )
        ));

        inMemoryRepository = new InMemoryRepositoryImpl(accounts);
        MovementService movementService = new MovementService(inMemoryRepository, new FakeEventBus());

        // act
        try {
            movementService.transfer(accounts.get(0).getUuid(), accounts.get(1).getUuid(), 200.00);
        } catch (AccountNotFound e) {
            throw new RuntimeException(e);
        }

        // assert
        assertEquals(1000.00, accounts.get(0).calculateBalance());
        assertEquals(700.00, accounts.get(1).calculateBalance());
    }

    @Test
    public void testTransferShouldSendEvents() {
        // arrange
        List<Account> accounts = new ArrayList<>(List.of(
                Account.create(new Owner("William", "Mote", "43957942C"),
                        new ArrayList<>(List.of(
                                new Movement(1000.00, MovementType.INCOME),
                                new Movement(200.00, MovementType.INCOME))
                        )
                ),
                Account.create(new Owner("Maria", "Garcia", "22392403V"),
                        new ArrayList<>(List.of(
                                new Movement(1000.00, MovementType.INCOME),
                                new Movement(500.00, MovementType.EXPENSE))
                        )
                )
        ));
        FakeEventBus fakeEventBus = new FakeEventBus();
        inMemoryRepository = new InMemoryRepositoryImpl(accounts);
        MovementService movementService = new MovementService(inMemoryRepository, fakeEventBus);

        // act
        try {
            movementService.transfer(accounts.get(0).getUuid(), accounts.get(1).getUuid(), 200.00);
        } catch (AccountNotFound e) {
            throw new RuntimeException(e);
        }

        // assert
        assertEquals(2, fakeEventBus.getEvents().size());
    }

    @Test
    public void testDeposit() {
        // arrange
        List<Account> accounts = new ArrayList<>(List.of(
                Account.create(new Owner("Maria", "Garcia", "22392403V"),
                        new ArrayList<>(List.of(
                                new Movement(1000.00, MovementType.INCOME),
                                new Movement(500.00, MovementType.EXPENSE))
                        )
                )
        ));

        inMemoryRepository = new InMemoryRepositoryImpl(accounts);
        MovementService movementService = new MovementService(inMemoryRepository, new FakeEventBus());

        // act
        try {
            movementService.deposit(accounts.get(0).getUuid(), 200.00);
        } catch (AccountNotFound e) {
            throw new RuntimeException(e);
        }

        // assert
        assertEquals(700.00, accounts.get(0).getBalance());
    }

    @Test
    public void testDepositShouldSendEvent() {
        // arrange
        List<Account> accounts = new ArrayList<>(List.of(
                Account.create(new Owner("Maria", "Garcia", "22392403V"),
                        new ArrayList<>(List.of(
                                new Movement(1000.00, MovementType.INCOME),
                                new Movement(500.00, MovementType.EXPENSE))
                        )
                )
        ));
        FakeEventBus fakeEventBus = new FakeEventBus();
        inMemoryRepository = new InMemoryRepositoryImpl(accounts);
        MovementService movementService = new MovementService(inMemoryRepository, fakeEventBus);

        // act
        try {
            movementService.deposit(accounts.get(0).getUuid(), 200.00);
        } catch (AccountNotFound e) {
            throw new RuntimeException(e);
        }

        // assert
        assertEquals(1, fakeEventBus.getEvents().size());
    }

    @Test
    public void testWithdraw() {
        // arrange
        List<Account> accounts = new ArrayList<>(List.of(
                Account.create(new Owner("Maria", "Garcia", "22392403V"),
                        new ArrayList<>(List.of(
                                new Movement(1000.00, MovementType.INCOME),
                                new Movement(500.00, MovementType.EXPENSE))
                        )
                ))
        );

        inMemoryRepository = new InMemoryRepositoryImpl(accounts);
        MovementService movementService = new MovementService(inMemoryRepository, new FakeEventBus());

        // act
        try {
            movementService.withdraw(accounts.get(0).getUuid(), 200.00);
        } catch (AccountNotFound e) {
            throw new RuntimeException(e);
        }

        // assert
        assertEquals(300.00, accounts.get(0).getBalance());
    }

    @Test
    public void testWithdrawShouldSendEvent() {
        // arrange
        List<Account> accounts = new ArrayList<>(List.of(
                Account.create(new Owner("Maria", "Garcia", "22392403V"),
                        new ArrayList<>(List.of(
                                new Movement(1000.00, MovementType.INCOME),
                                new Movement(500.00, MovementType.EXPENSE))
                        )
                ))
        );

        FakeEventBus fakeEventBus = new FakeEventBus();
        inMemoryRepository = new InMemoryRepositoryImpl(accounts);
        MovementService movementService = new MovementService(inMemoryRepository, fakeEventBus);

        // act
        try {
            movementService.withdraw(accounts.get(0).getUuid(), 200.00);
        } catch (AccountNotFound e) {
            throw new RuntimeException(e);
        }

        // assert
        assertEquals(1, fakeEventBus.getEvents().size());
    }
}
