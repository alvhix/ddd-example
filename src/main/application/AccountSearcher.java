package main.application;

import main.domain.Account;
import main.domain.AccountRepository;
import main.domain.Movement;

import java.util.List;

public final class AccountSearcher {
    private final AccountRepository accountRepository;

    public AccountSearcher(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAllAccounts() {
        return this.accountRepository.getAllAccounts();
    }

    public List<Movement> getAllMovements(String uuid) {
        return this.accountRepository.getAllMovements(uuid);
    }
}
