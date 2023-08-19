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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@Execution(ExecutionMode.CONCURRENT)
public final class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountSearcher accountSearcher;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllAccounts() {
        // arrange
        List<Account> accounts = IntStream.range(0, 4)
                .mapToObj(i -> AccountMother.create())
                .collect(Collectors.toList());
        when(accountRepository.all()).thenReturn(accounts);

        // act
        List<Account> accountsResult = accountSearcher.all();

        // assert
        assertEquals(accounts, accountsResult);
    }

    @Test
    public void testGetAllMovements() throws AccountNotFound {
        // arrange
        Account account = AccountMother.create();
        when(accountRepository.get(account.uuid())).thenReturn(Optional.of(account));

        // act
        Set<Movement> movements = accountSearcher.allMovements(account.uuid());

        // assert
        assertEquals(account.movements(), movements);
    }
}
