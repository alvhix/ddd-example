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
@TestMethodOrder(MethodOrderer.Random.class)
public class MariaDBAccountRepositoryTest {
    private AccountRepository accountRepository;

    private static final MariaDBContainer<?> mariadb = new MariaDBContainer<>("mariadb:lts");

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
        Account account = AccountMother.create();
        accountRepository.save(account);
    }

    @Test
    @Transactional
    void testAll() {
        // arrange
        Account account = AccountMother.create();
        accountRepository.save(account);

        // act
        List<Account> accountList = accountRepository.all();

        // assert
        assertEquals(account, accountList.get(0));
    }

    @Test
    @Transactional
    void testGet() {
        // arrange
        Account account = AccountMother.create();
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
        Account account = AccountMother.create();
        accountRepository.save(account);
        Double balance = account.balance();

        // act
        account.addMovement(400.00, MovementType.EXPENSE);
        accountRepository.update(account);

        // assert
        assertTrue(accountRepository.get(account.uuid()).isPresent());
        assertEquals(balance - 400.0, accountRepository.get(account.uuid()).get().balance());
    }
}
