package main.account.domain;

import java.util.List;

public interface AccountRepository {
    List<Account> getAllAccounts();
    List<Movement> getAllMovements(String uuid);
    void transfer(String origin, String destination, Double amount);
    Double deposit(String account, Double amount);
    Double withdraw(String account, Double amount);
}
