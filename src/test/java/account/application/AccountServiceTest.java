package account.application;

import account.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@Execution(ExecutionMode.SAME_THREAD)
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
                        new HashSet<>(Set.of(
                                Movement.create(1000.00, MovementType.INCOME),
                                Movement.create(200.00, MovementType.INCOME))
                        )
                ),
                Account.create(Owner.create("Maria", "Garcia", "22392403V"),
                        new HashSet<>(Set.of(
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
        assertEquals(accounts, accountsResult);
    }

    @Test
    public void testGetAllMovements() throws AccountNotFound {
        // arrange
        Account account = accounts.get(0);
        when(accountRepository.get(account.uuid())).thenReturn(Optional.of(account));

        // act
        Set<Movement> movements = accountSearcher.allMovements(account.uuid());

        // assert
        assertEquals(account.movements(), movements);
    }
}
