package account.infrastructure;

import account.domain.*;
import account.infrastructure.persistence.MySqlAccountRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MySqlAccountRepositoryTest {
    private AccountRepository accountRepository;
    private List<Account> accounts;

    @BeforeEach
    public void setup() {
        this.accountRepository = new MySqlAccountRepositoryImpl();
        this.accounts = List.of(Account.create(
                Owner.create("Teodoro", "Vara", "12345678H"),
                new ArrayList<>(List.of(
                        Movement.create(1000.00, MovementType.INCOME),
                        Movement.create(200.00, MovementType.INCOME))
                ))
        );
    }

    @Test
    public void testSaveAccount() {
        this.accountRepository.save(accounts.get(0));
    }

    @Test
    public void testGet() {
        // arrange
        Account account = accounts.get(0);
        this.accountRepository.save(account);

        // act
        Optional<Account> accountOptional = this.accountRepository.get(account.uuid());

        // assert
        assertTrue(accountOptional.isPresent());
    }

    @Test
    public void testUpdate() {
        // arrange
        Account account = accounts.get(0);
        this.accountRepository.save(account);

        // act
        account.addMovement(400.00, MovementType.EXPENSE);
        this.accountRepository.update(account);

        // assert
        assertTrue(this.accountRepository.get(account.uuid()).isPresent());
        assertEquals(3, this.accountRepository.get(account.uuid()).get().movements().size());
    }
}