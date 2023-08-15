package account.infrastructure;

import account.domain.*;
import account.infrastructure.persistence.MariaDBAccountRepositoryImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.testcontainers.containers.MariaDBContainer;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Execution(ExecutionMode.SAME_THREAD)
public class MariaDBAccountRepositoryTest {
    private AccountRepository accountRepository;
    private final List<Account> accounts = List.of(Account.create(
            Owner.create("Teodoro", "Vara", "12345678H"),
            new HashSet<>(Set.of(
                    Movement.create(1000.00, MovementType.INCOME),
                    Movement.create(200.00, MovementType.INCOME))
            ))
    );

    private static final MariaDBContainer<?> mariadb = new MariaDBContainer<>(
            "mariadb:lts"
    );

    @BeforeAll
    static void beforeAll() {
        mariadb.start();
    }

    @BeforeEach
    void setup() {
        this.accountRepository = new MariaDBAccountRepositoryImpl(mariadb);
    }

    @AfterAll
    static void afterAll() {
        mariadb.stop();
    }

    @Test
    @Transactional
    void testSaveAccount() {
        accountRepository.save(accounts.get(0));
    }

    @Test
    @Transactional
    void testAll() {
        // arrange
        accountRepository.save(accounts);

        // act
        List<Account> accountList = accountRepository.all();

        // assert
        assertEquals(accounts.get(0), accountList.get(0));
    }

    @Test
    @Transactional
    void testGet() {
        // arrange
        Account account = accounts.get(0);
        accountRepository.save(account);

        // act
        Optional<Account> accountOptional = accountRepository.get(account.uuid());

        // assert
        assertTrue(accountOptional.isPresent());
        assertEquals(account, accountOptional.get());
    }

    @Test
    @Transactional
    void testUpdate() {
        // arrange
        Account account = accounts.get(0);
        accountRepository.save(account);

        // act
        account.addMovement(400.00, MovementType.EXPENSE);
        accountRepository.update(account);

        // assert
        assertTrue(accountRepository.get(account.uuid()).isPresent());
        assertEquals(3, accountRepository.get(account.uuid()).get().movements().size());
    }
}
