package main.account.domain;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    List<Account> getAllAccounts();
    Optional<Account> getAccount(String uuid);
    void update(Account account);
}
