package main.domain;

import java.util.List;

public interface AccountRepository {
    List<Account> getAllAccounts();
    List<Movement> getAllMovements(String uuid);
    void transfer(String origin, String destination, Double quantity);
    Double deposit(String account, Double quantity);
    Double withdraw(String account, Double quantity);
}
