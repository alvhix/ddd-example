import application.AccountSearcher;
import application.TransferenceService;
import domain.Account;
import domain.Movement;
import domain.MovementType;
import domain.Owner;
import infrastructure.controller.CliController;
import infrastructure.persistence.InMemoryRepositoryImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account("ff6f9c90-bbb7-409c-87d2-04277f85d111",
                new Owner("William", "Mote", "43957942C"),
                new ArrayList<>(Arrays.asList(new Movement(1000.00, MovementType.INCOME),
                        new Movement(200.00, MovementType.INCOME)))));

        accounts.add(new Account("249c9b83-4912-4719-9d5d-a27a3b4c4a8c",
                new Owner("Maria", "Garcia", "22392403V"),
                new ArrayList<>(Arrays.asList(new Movement(1000.00, MovementType.INCOME)))));

        CliController cliController = new CliController(new AccountSearcher(new InMemoryRepositoryImpl(accounts)),
                new TransferenceService(new InMemoryRepositoryImpl(accounts)));

        System.out.println(cliController.all());
        System.out.println(cliController.getAllMovements("ff6f9c90-bbb7-409c-87d2-04277f85d111"));
        cliController.transfer("ff6f9c90-bbb7-409c-87d2-04277f85d111", "249c9b83-4912-4719-9d5d-a27a3b4c4a8c", 600.00);
        System.out.println(cliController.all());
    }
}