package account.infrastructure.persistence;

import account.domain.Account;
import account.domain.AccountRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class InMemoryAccountRepositoryImpl implements AccountRepository {
    private final List<Account> accounts;

    public InMemoryAccountRepositoryImpl() {
        this.accounts = new ArrayList<>();
    }

    @Override
    public List<Account> all() {
        return accounts;
    }

    @Override
    public Optional<Account> get(UUID uuid) {
        return accounts.stream().filter(x -> x.uuid().equals(uuid)).findFirst();
    }

    @Override
    public void save(Account account) {
        accounts.add(account);
    }

    @Override
    public void save(List<Account> accounts) {
        accounts.forEach(this::save);
    }

    @Override
    public void update(Account account) {
        accounts.stream().filter(x -> x.uuid().equals(account.uuid())).findFirst().map(x -> account);
    }
}
