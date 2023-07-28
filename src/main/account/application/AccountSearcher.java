package main.account.application;

import main.account.domain.*;

import java.util.List;

public final class AccountSearcher {
    private final AccountRepository accountRepository;
    private final AccountFinder accountFinder;

    public AccountSearcher(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        this.accountFinder = new AccountFinder(accountRepository);
    }

    public List<Account> getAllAccounts() {
        return this.accountRepository.getAllAccounts();
    }

    public List<Movement> getAllMovements(String uuid) throws AccountNotFound {
        this.accountFinder.find(uuid);
        return this.accountRepository.getAllMovements(uuid);
    }

    public Account getAccount(String uuid) throws AccountNotFound {
        return this.accountFinder.find(uuid);
    }
}
