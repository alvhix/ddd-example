package application;

import domain.Account;
import domain.AccountRepository;
import domain.Movement;

import java.util.List;

public final class AccountSearcher {
    private final AccountRepository accountRepository;

    public AccountSearcher(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAllAccounts() {
        return this.accountRepository.all();
    }

    public List<Movement> getAllMovements(String uuid) {
        return this.accountRepository.getAllMovements(uuid);
    }
}
