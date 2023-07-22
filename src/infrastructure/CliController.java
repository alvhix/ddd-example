package infrastructure;

import application.AccountSearcher;
import domain.Account;
import domain.Movement;

import java.util.Collections;
import java.util.List;

public final class CliController {
    private AccountSearcher accountSearcher;

    public CliController(AccountSearcher accountSearcher) {
        this.accountSearcher = accountSearcher;
    }

    public List<Account> all() {
        return this.accountSearcher.getAllAccounts();
    }

    public List<Movement> getAllMovements(String uuid) {
        return this.accountSearcher.getAllMovements(uuid);
    }
}
