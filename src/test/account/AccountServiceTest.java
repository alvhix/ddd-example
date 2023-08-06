package test.account;

import main.account.application.AccountSearcher;
import main.account.application.MovementService;
import main.account.domain.*;
import main.account.infrastructure.persistence.InMemoryRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.account.infrastructure.FakeEventBus;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class AccountServiceTest {

    private InMemoryRepositoryImpl inMemoryRepository;

    @BeforeEach
    public void setup() {
        this.inMemoryRepository = new InMemoryRepositoryImpl();
    }

    @Test
    public void testGetAllAccounts() {
        // arrange
        List<Account> accounts = new ArrayList<>(List.of(
                Account.create(Owner.create("Teodoro", "Rodríguez", "12345678H"), List.of(Movement.create(540.0, MovementType.INCOME))),
                Account.create(Owner.create(null, null, null), List.of(Movement.create(300.0, MovementType.EXPENSE))),
                Account.create(Owner.create(null, null, null), List.of(Movement.create(170.5, MovementType.EXPENSE))),
                Account.create(Owner.create(null, null, null), List.of(Movement.create(42.24, MovementType.EXPENSE)))
        ));

        inMemoryRepository.save(accounts);
        AccountSearcher accountSearcher = new AccountSearcher(inMemoryRepository);

        // act
        List<Account> accountsResult = accountSearcher.all();

        // assert
        assertEquals(4, accountsResult.size());
    }

    @Test
    public void testGetAllMovements() {
        // arrange
        List<Account> accounts = new ArrayList<>(List.of(
                Account.create(Owner.create("Maria", "Garcia", "22392403V"),
                        new ArrayList<>(List.of(
                                Movement.create(1000.00, MovementType.INCOME),
                                Movement.create(500.00, MovementType.EXPENSE))
                        )
                )
        ));

        inMemoryRepository.save(accounts);
        AccountSearcher accountSearcher = new AccountSearcher(inMemoryRepository);

        // act
        List<Movement> movements;
        try {
            movements = accountSearcher.allMovements(accounts.get(0).uuid());
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
                Account.create(Owner.create("William", "Mote", "43957942C"),
                        new ArrayList<>(List.of(
                                Movement.create(1000.00, MovementType.INCOME),
                                Movement.create(200.00, MovementType.INCOME))
                        )
                ),
                Account.create(Owner.create("Maria", "Garcia", "22392403V"),
                        new ArrayList<>(List.of(
                                Movement.create(1000.00, MovementType.INCOME),
                                Movement.create(500.00, MovementType.EXPENSE))
                        )
                )
        ));

        inMemoryRepository.save(accounts);
        MovementService movementService = new MovementService(inMemoryRepository, new FakeEventBus());

        // act
        try {
            movementService.transfer(accounts.get(0).uuid(), accounts.get(1).uuid(), 200.00);
        } catch (AccountNotFound e) {
            throw new RuntimeException(e);
        }

        // assert
        assertEquals(1200.00, accounts.get(0).calculateBalance());
        assertEquals(500.00, accounts.get(1).calculateBalance());
    }

    @Test
    public void testTransferShouldSendEvents() {
        // arrange
        List<Account> accounts = new ArrayList<>(List.of(
                Account.create(Owner.create("William", "Mote", "43957942C"),
                        new ArrayList<>(List.of(
                                Movement.create(1000.00, MovementType.INCOME),
                                Movement.create(200.00, MovementType.INCOME))
                        )
                ),
                Account.create(Owner.create("Maria", "Garcia", "22392403V"),
                        new ArrayList<>(List.of(
                                Movement.create(1000.00, MovementType.INCOME),
                                Movement.create(500.00, MovementType.EXPENSE))
                        )
                )
        ));
        FakeEventBus fakeEventBus = new FakeEventBus();

        inMemoryRepository.save(accounts);
        MovementService movementService = new MovementService(inMemoryRepository, fakeEventBus);

        // act
        try {
            movementService.transfer(accounts.get(0).uuid(), accounts.get(1).uuid(), 200.00);
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
                Account.create(Owner.create("Maria", "Garcia", "22392403V"),
                        new ArrayList<>(List.of(
                                Movement.create(1000.00, MovementType.INCOME),
                                Movement.create(500.00, MovementType.EXPENSE))
                        )
                )
        ));

        inMemoryRepository.save(accounts);
        MovementService movementService = new MovementService(inMemoryRepository, new FakeEventBus());

        // act
        try {
            movementService.deposit(accounts.get(0).uuid(), 200.00);
        } catch (AccountNotFound e) {
            throw new RuntimeException(e);
        }

        // assert
        assertEquals(700.00, inMemoryRepository.all().get(0).balance());
    }

    @Test
    public void testDepositShouldSendEvent() {
        // arrange
        List<Account> accounts = new ArrayList<>(List.of(
                Account.create(Owner.create("Maria", "Garcia", "22392403V"),
                        new ArrayList<>(List.of(
                                Movement.create(1000.00, MovementType.INCOME),
                                Movement.create(500.00, MovementType.EXPENSE))
                        )
                )
        ));
        FakeEventBus fakeEventBus = new FakeEventBus();

        inMemoryRepository.save(accounts);
        MovementService movementService = new MovementService(inMemoryRepository, fakeEventBus);

        // act
        try {
            movementService.deposit(accounts.get(0).uuid(), 200.00);
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
                Account.create(Owner.create("Maria", "Garcia", "22392403V"),
                        new ArrayList<>(List.of(
                                Movement.create(1000.00, MovementType.INCOME),
                                Movement.create(500.00, MovementType.EXPENSE))
                        )
                )
        ));

        inMemoryRepository.save(accounts);
        MovementService movementService = new MovementService(inMemoryRepository, new FakeEventBus());

        // act
        try {
            movementService.withdraw(accounts.get(0).uuid(), 200.00);
        } catch (AccountNotFound e) {
            throw new RuntimeException(e);
        }

        // assert
        assertEquals(300.00, inMemoryRepository.all().get(0).balance());
    }

    @Test
    public void testWithdrawShouldSendEvent() {
        // arrange
        List<Account> accounts = new ArrayList<>(List.of(
                Account.create(Owner.create("Maria", "Garcia", "22392403V"),
                        new ArrayList<>(List.of(
                                Movement.create(1000.00, MovementType.INCOME),
                                Movement.create(500.00, MovementType.EXPENSE))
                        )
                )
        ));

        FakeEventBus fakeEventBus = new FakeEventBus();

        inMemoryRepository.save(accounts);
        MovementService movementService = new MovementService(inMemoryRepository, fakeEventBus);

        // act
        try {
            movementService.withdraw(accounts.get(0).uuid(), 200.00);
        } catch (AccountNotFound e) {
            throw new RuntimeException(e);
        }

        // assert
        assertEquals(1, fakeEventBus.getEvents().size());
    }
}
