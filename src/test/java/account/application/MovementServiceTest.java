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
    @InjectMocks
    private MovementService movementService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testTransfer() throws AccountNotFound {
        // arrange
        Account origin = AccountMother.create();
        Account destination = AccountMother.create();

        when(accountRepository.get(origin.uuid())).thenReturn(Optional.of(origin));
        when(accountRepository.get(destination.uuid())).thenReturn(Optional.of(destination));

        Double originAmount = origin.balance();
        Double destinationAmount = destination.balance();

        // act
        movementService.transfer(origin.uuid(), destination.uuid(), 200.00);

        // assert
        assertEquals(originAmount - 200.0, origin.balance());
        assertEquals(destinationAmount + 200.0, destination.balance());
    }

    @Test
    public void testTransferShouldUpdate() throws AccountNotFound {
        // arrange
        Account origin = AccountMother.create();
        Account destination = AccountMother.create();

        when(accountRepository.get(origin.uuid())).thenReturn(Optional.of(origin));
        when(accountRepository.get(destination.uuid())).thenReturn(Optional.of(destination));

        // act
        movementService.transfer(origin.uuid(), destination.uuid(), 200.00);

        // assert
        verify(accountRepository, times(2)).update(any());
    }

    @Test
    public void testTransferShouldSendEvents() throws AccountNotFound {
        // arrange
        Account origin = AccountMother.create();
        Account destination = AccountMother.create();

        when(accountRepository.get(origin.uuid())).thenReturn(Optional.of(origin));
        when(accountRepository.get(destination.uuid())).thenReturn(Optional.of(destination));

        // act
        movementService.transfer(origin.uuid(), destination.uuid(), 200.00);

        // assert
        verify(eventBus, times(2)).publish(any());
    }

    @Test
    public void testDeposit() throws AccountNotFound {
        // arrange
        Account account = AccountMother.create();
        Double previousBalance = account.balance();
        when(accountRepository.get(account.uuid())).thenReturn(Optional.of(account));

        // act
        movementService.deposit(account.uuid(), 200.00);

        // assert
        assertEquals(previousBalance + 200.0, account.balance());
    }

    @Test
    public void testDepositShouldUpdate() throws AccountNotFound {
        // arrange
        Account account = AccountMother.create();
        when(accountRepository.get(account.uuid())).thenReturn(Optional.of(account));

        // act
        movementService.deposit(account.uuid(), 200.00);

        // assert
        verify(accountRepository, times(1)).update(any());
    }

    @Test
    public void testDepositShouldSendEvent() throws AccountNotFound {
        // arrange
        Account account = AccountMother.create();
        when(accountRepository.get(account.uuid())).thenReturn(Optional.of(account));

        // act
        movementService.deposit(account.uuid(), 200.00);

        // assert
        verify(eventBus, times(1)).publish(any());
    }

    @Test
    public void testWithdraw() throws AccountNotFound {
        // arrange
        Account account = AccountMother.create();
        Double previousBalance = account.balance();
        when(accountRepository.get(account.uuid())).thenReturn(Optional.of(account));

        // act
        movementService.withdraw(account.uuid(), 200.00);

        // assert
        assertEquals(previousBalance - 200.0, account.balance());
    }

    @Test
    public void testWithdrawShouldUpdate() throws AccountNotFound {
        // arrange
        Account account = AccountMother.create();
        when(accountRepository.get(account.uuid())).thenReturn(Optional.of(account));

        // act
        movementService.withdraw(account.uuid(), 200.00);

        // assert
        verify(accountRepository, times(1)).update(any());
    }

    @Test
    public void testWithdrawShouldSendEvent() throws AccountNotFound {
        // arrange
        Account account = AccountMother.create();
        when(accountRepository.get(account.uuid())).thenReturn(Optional.of(account));

        // act
        movementService.withdraw(account.uuid(), 200.00);

        // assert
        verify(eventBus, times(1)).publish(any());
    }
}
