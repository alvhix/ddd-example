package infrastructure.persistence;

import domain.Account;
import domain.AccountRepository;
import domain.Movement;
import domain.MovementType;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    @Override
    public void transfer(String origin, String destination, Double quantity) {
        Optional<Account> originAccount = this.accounts.stream()
                .filter(account -> account.getUuid()
                        .equals(origin)).findFirst();
        Optional<Account> destinationAccount = this.accounts.stream()
                .filter(account -> account.getUuid()
                        .equals(destination)).findFirst();

        if (originAccount.isPresent() && destinationAccount.isPresent()) {
            originAccount.get().addMovement(quantity, MovementType.EXPENSE);
            destinationAccount.get().addMovement(quantity, MovementType.INCOME);
        }
    }
}
