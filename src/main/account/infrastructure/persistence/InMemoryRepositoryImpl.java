package main.account.infrastructure.persistence;

import main.account.domain.Account;
import main.account.domain.AccountRepository;
import main.account.domain.Movement;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class InMemoryRepositoryImpl implements AccountRepository {
    private final List<Account> accounts;

    public InMemoryRepositoryImpl(List<Account> accounts) {
        this.accounts = accounts;
    }

    public List<Account> getAllAccounts() {
        return this.accounts;
    }

    @Override
    public List<Movement> getAllMovements(String uuid) {
        return this.accounts.stream().filter(acc -> acc.getUuid().equals(uuid)).findFirst().map(Account::getMovements).orElse(Collections.emptyList());
    }

    @Override
    public Optional<Account> getAccount(String uuid) {
        return this.accounts.stream().filter(account -> account.getUuid()
                .equals(uuid)).findFirst();
    }

    @Override
    public void update(Account account) {
        getAccount(account.getUuid()).ifPresent(existingAccount -> {
            int index = this.accounts.indexOf(existingAccount);
            this.accounts.set(index, account);
        });
    }
}
