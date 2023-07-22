import application.AccountSearcher;
import domain.Account;
import domain.Movement;
import domain.MovementType;
import domain.Owner;
import infrastructure.CliController;
import infrastructure.InMemoryRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account("ff6f9c90-bbb7-409c-87d2-04277f85d111",
                new Owner("William", "Mote", "43957942C"),
                List.of(new Movement(1000.00, MovementType.INCOME),
                        new Movement(200.00, MovementType.INCOME))));


        CliController cliController = new CliController(new AccountSearcher(new InMemoryRepositoryImpl(accounts)));

        System.out.println(cliController.all());
        System.out.println(cliController.getAllMovements("ff6f9c90-bbb7-409c-87d2-04277f85d111"));
    }
}