package main.account.infrastructure.persistence;

import main.account.domain.Account;
import main.account.domain.AccountRepository;

import java.util.List;
import java.util.Optional;

public final class InMemoryRepositoryImpl implements AccountRepository {
    private final List<Account> accounts;

    public InMemoryRepositoryImpl(List<Account> accounts) {
        this.accounts = accounts;
    }

    public List<Account> all() {
        return this.accounts;
    }

    @Override
    public Optional<Account> get(String uuid) {
        return this.accounts.stream().filter(account -> account.uuid()
                .equals(uuid)).findFirst();
    }

    @Override
    public void update(Account account) {
        get(account.uuid()).ifPresent(existingAccount -> {
            int index = this.accounts.indexOf(existingAccount);
            this.accounts.set(index, account);
        });
    }
}
