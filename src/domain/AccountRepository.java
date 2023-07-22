package domain;

import java.util.List;

public interface AccountRepository {
    List<Account> all();
    List<Movement> getAllMovements(String uuid);
    void transfer(String origin, String destination, Double quantity);
}
