package test.account.application;

import main.account.application.AccountSearcher;
import main.account.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


public final class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;
    private List<Account> accounts;
    @InjectMocks
    private AccountSearcher accountSearcher;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this); // Initialize the mocks
        this.accounts = new ArrayList<>(List.of(
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
    }

    @Test
    public void testGetAllAccounts() {
        // arrange
        when(accountRepository.all()).thenReturn(accounts);

        // act
        List<Account> accountsResult = accountSearcher.all();

        // assert
        assertEquals(2, accountsResult.size());
    }

    @Test
    public void testGetAllMovements() throws AccountNotFound {
        // arrange
        when(accountRepository.get(accounts.get(0).uuid())).thenReturn(Optional.of(accounts.get(0)));

        // act
        List<Movement> movements = accountSearcher.allMovements(accounts.get(0).uuid());

        // assert
        assertEquals(2, movements.size());
    }
}
