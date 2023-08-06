package main.account.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    List<Account> all();

    Optional<Account> get(UUID uuid);

    void save(Account account);

    void save(List<Account> accounts);

    void update(Account account);
}
