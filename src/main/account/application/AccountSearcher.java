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

    public List<Account> all() {
        return this.accountRepository.all();
    }

    public Account get(String uuid) throws AccountNotFound {
        return this.accountFinder.find(uuid);
    }

    public List<Movement> allMovements(String uuid) throws AccountNotFound {
        return this.accountFinder.find(uuid).movements();
    }
}
