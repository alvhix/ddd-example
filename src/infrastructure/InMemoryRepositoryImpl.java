package infrastructure;

import application.AccountSearcher;
import domain.Account;
import domain.AccountRepository;
import domain.Movement;

import java.util.Collections;
import java.util.List;

public final class InMemoryRepositoryImpl implements AccountRepository {
    private final List<Account> accounts;

    public InMemoryRepositoryImpl(List<Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public List<Account> all() {
        return this.accounts;
    }

    @Override
    public List<Movement> getAllMovements(String uuid) {
        return this.accounts.stream()
                .filter(acc -> acc.getUuid().equals(uuid))
                .findFirst().map(Account::getMovements)
                .orElse(Collections.emptyList());
    }
}
