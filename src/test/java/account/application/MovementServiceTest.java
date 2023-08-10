package account.application;

import account.domain.*;
import shared.domain.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MovementServiceTest {
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
    public void testTransfer() throws AccountNotFound {
        // arrange
        when(accountRepository.get(accounts.get(0).uuid())).thenReturn(Optional.of(accounts.get(0)));
        when(accountRepository.get(accounts.get(1).uuid())).thenReturn(Optional.of(accounts.get(1)));

        // act
        movementService.transfer(accounts.get(0).uuid(), accounts.get(1).uuid(), 200.00);

        // assert
        assertEquals(200.0, accounts.get(0).lastMovement().amount());
        assertEquals(200.0, accounts.get(1).lastMovement().amount());
        assertEquals(MovementType.EXPENSE, accounts.get(0).lastMovement().type());
        assertEquals(MovementType.INCOME, accounts.get(1).lastMovement().type());
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
        when(accountRepository.get(accounts.get(0).uuid())).thenReturn(Optional.of(accounts.get(0)));

        // act
        movementService.deposit(accounts.get(0).uuid(), 200.00);

        // assert
        assertEquals(1400.0, accounts.get(0).balance());
        assertEquals(200, accounts.get(0).lastMovement().amount());
        assertEquals(MovementType.INCOME, accounts.get(0).lastMovement().type());
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
        when(accountRepository.get(accounts.get(0).uuid())).thenReturn(Optional.of(accounts.get(0)));

        // act
        movementService.withdraw(accounts.get(0).uuid(), 200.00);

        // assert
        assertEquals(1000.00, accounts.get(0).balance());
        assertEquals(200, accounts.get(0).lastMovement().amount());
        assertEquals(MovementType.EXPENSE, accounts.get(0).lastMovement().type());
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
