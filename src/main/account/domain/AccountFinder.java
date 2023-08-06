package main.account.domain;

import java.util.Optional;

public class AccountFinder {

    private final AccountRepository accountRepository;

    public AccountFinder(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account find(String uuid) throws AccountNotFound {
        Optional<Account> account = this.accountRepository.get(uuid);

        if (!account.isPresent()) throw new AccountNotFound(uuid);

        return account.get();
    }
}
