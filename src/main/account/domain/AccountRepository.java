package main.account.domain;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    List<Account> all();
    Optional<Account> get(String uuid);
    void update(Account account);
}
