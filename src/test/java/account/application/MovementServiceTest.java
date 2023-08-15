package account.application;

import account.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import shared.domain.EventBus;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Execution(ExecutionMode.CONCURRENT)
public final class MovementServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private EventBus eventBus;
    private List<Account> accounts;
    @InjectMocks
    private MovementService movementService;

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
    public void testTransfer() throws AccountNotFound {
        // arrange
        Account origin = accounts.get(0);
        Account destination = accounts.get(1);
        when(accountRepository.get(origin.uuid())).thenReturn(Optional.of(origin));
        when(accountRepository.get(destination.uuid())).thenReturn(Optional.of(destination));

        // act
        movementService.transfer(origin.uuid(), destination.uuid(), 200.00);

        // assert
        assertEquals(1000.0, origin.balance());
        assertEquals(700.0, destination.balance());
        assertEquals(3, origin.movements().size());
        assertEquals(3, destination.movements().size());
    }

    @Test
    public void testTransferShouldUpdate() throws AccountNotFound {
        // arrange
        when(accountRepository.get(accounts.get(0).uuid())).thenReturn(Optional.of(accounts.get(0)));
        when(accountRepository.get(accounts.get(1).uuid())).thenReturn(Optional.of(accounts.get(1)));

        // act
        movementService.transfer(accounts.get(0).uuid(), accounts.get(1).uuid(), 200.00);

        // assert
        verify(accountRepository, times(2)).update(any());
    }

    @Test
    public void testTransferShouldSendEvents() throws AccountNotFound {
        // arrange
        when(accountRepository.get(accounts.get(0).uuid())).thenReturn(Optional.of(accounts.get(0)));
        when(accountRepository.get(accounts.get(1).uuid())).thenReturn(Optional.of(accounts.get(1)));

        // act
        movementService.transfer(accounts.get(0).uuid(), accounts.get(1).uuid(), 200.00);

        // assert
        verify(eventBus, times(2)).publish(any());
    }

    @Test
    public void testDeposit() throws AccountNotFound {
        // arrange
        Account account = accounts.get(0);
        when(accountRepository.get(account.uuid())).thenReturn(Optional.of(account));

        // act
        movementService.deposit(account.uuid(), 200.00);

        // assert
        assertEquals(1400.0, account.balance());
        assertEquals(3, account.movements().size());
    }

    @Test
    public void testDepositShouldUpdate() throws AccountNotFound {
        // arrange
        when(accountRepository.get(accounts.get(0).uuid())).thenReturn(Optional.of(accounts.get(0)));

        // act
        movementService.deposit(accounts.get(0).uuid(), 200.00);

        // assert
        verify(accountRepository, times(1)).update(any());
    }

    @Test
    public void testDepositShouldSendEvent() throws AccountNotFound {
        // arrange
        when(accountRepository.get(accounts.get(0).uuid())).thenReturn(Optional.of(accounts.get(0)));

        // act
        movementService.deposit(accounts.get(0).uuid(), 200.00);

        // assert
        verify(eventBus, times(1)).publish(any());
    }

    @Test
    public void testWithdraw() throws AccountNotFound {
        // arrange
        Account account = accounts.get(0);
        when(accountRepository.get(account.uuid())).thenReturn(Optional.of(account));

        // act
        movementService.withdraw(account.uuid(), 200.00);

        // assert
        assertEquals(1000.00, account.balance());
        assertEquals(3, account.movements().size());
    }

    @Test
    public void testWithdrawShouldUpdate() throws AccountNotFound {
        // arrange
        when(accountRepository.get(accounts.get(0).uuid())).thenReturn(Optional.of(accounts.get(0)));

        // act
        movementService.withdraw(accounts.get(0).uuid(), 200.00);

        // assert
        verify(accountRepository, times(1)).update(any());
    }

    @Test
    public void testWithdrawShouldSendEvent() throws AccountNotFound {
        // arrange
        when(accountRepository.get(accounts.get(0).uuid())).thenReturn(Optional.of(accounts.get(0)));

        // act
        movementService.withdraw(accounts.get(0).uuid(), 200.00);

        // assert
        verify(eventBus, times(1)).publish(any());
    }
}
