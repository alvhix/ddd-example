package main.account.infrastructure.controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.account.application.AccountSearcher;
import main.account.application.MovementService;
import main.account.domain.*;
import main.account.infrastructure.dto.MovementDto;
import main.account.infrastructure.dto.MovementSuccessDto;
import main.account.infrastructure.persistence.MySqlAccountRepositoryImpl;
import main.shared.infrastructure.bus.GuavaEventBus;
import main.shared.infrastructure.http.HttpController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class RestController implements HttpHandler {
    private final static Integer OK = 200;
    private final static Integer NOT_FOUND = 404;
    private final AccountSearcher accountSearcher;
    private final MovementService movementService;
    private final Gson gson;

    public RestController() {
        List<Account> accounts = new ArrayList<>(List.of(
                Account.create(Owner.create("William", "Mote", "43957942C"),
                        new ArrayList<>(List.of(
                                Movement.create(500.0, MovementType.INCOME),
                                Movement.create(200.0, MovementType.INCOME))
                        )
                ),
                Account.create(Owner.create("Maria", "Garcia", "22392403V"),
                        new ArrayList<>(List.of(
                                Movement.create(1000.00, MovementType.INCOME),
                                Movement.create(500.00, MovementType.EXPENSE))
                        )
                )
        ));
        AccountRepository accountRepository = new MySqlAccountRepositoryImpl();
        accountRepository.save(accounts);
        this.accountSearcher = new AccountSearcher(accountRepository);
        this.movementService = new MovementService(accountRepository, new GuavaEventBus());
        this.gson = new Gson();
    }

    @Override
    public void handle(HttpExchange httpExchange) {
        String action = httpExchange.getRequestURI().getPath().split("/")[2];
        Map<String, String> query = HttpController.queryToMap(httpExchange.getRequestURI().getQuery());
        switch (action) {
            case "getAll":
                getAllAccounts(httpExchange);
                break;
            case "getMovements":
                getAllMovements(httpExchange, query.get("uuid"));
                break;
            case "get":
                getAccount(httpExchange, query.get("uuid"));
                break;
            case "deposit":
                deposit(httpExchange, HttpController.getRequestBody(httpExchange));
                break;
            case "withdraw":
                withdraw(httpExchange, HttpController.getRequestBody(httpExchange));
                break;
            case "transfer":
                transfer(httpExchange, HttpController.getRequestBody(httpExchange));
                break;
            default:
                HttpController.sendResponse(httpExchange, "Operation not found", NOT_FOUND);
        }
    }

    private void getAllAccounts(HttpExchange httpExchange) {
        if (!httpExchange.getRequestMethod().equals("GET")) {
            return;
        }

        HttpController.sendResponse(httpExchange, this.gson.toJson(this.accountSearcher.all()), OK);
    }

    private void getAllMovements(HttpExchange httpExchange, String uuid) {
        if (!httpExchange.getRequestMethod().equals("GET")) {
            return;
        }

        try {
            HttpController.sendResponse(httpExchange, this.gson.toJson(this.accountSearcher.allMovements(UUID.fromString(uuid))), OK);
        } catch (AccountNotFound e) {
            HttpController.sendResponse(httpExchange, "Account not found", NOT_FOUND);
        }
    }

    private void getAccount(HttpExchange httpExchange, String uuid) {
        if (!httpExchange.getRequestMethod().equals("GET")) {
            return;
        }

        try {
            Account account = this.accountSearcher.get(UUID.fromString(uuid));
            HttpController.sendResponse(httpExchange, this.gson.toJson(account), OK);
        } catch (AccountNotFound e) {
            HttpController.sendResponse(httpExchange, "Account not found", NOT_FOUND);
        }
    }

    private void deposit(HttpExchange httpExchange, String request) {
        if (!httpExchange.getRequestMethod().equals("POST")) {
            return;
        }

        MovementDto movementDto = this.gson.fromJson(request, MovementDto.class);
        try {
            this.movementService.deposit(movementDto.origin, movementDto.amount);
            HttpController.sendResponse(httpExchange, this.gson.toJson(new MovementSuccessDto("Deposit completed successfully")), OK);
        } catch (AccountNotFound e) {
            HttpController.sendResponse(httpExchange, "Account not found", NOT_FOUND);
        }
    }

    private void withdraw(HttpExchange httpExchange, String request) {
        if (!httpExchange.getRequestMethod().equals("POST")) {
            return;
        }

        MovementDto movementDto = this.gson.fromJson(request, MovementDto.class);
        try {
            this.movementService.withdraw(movementDto.origin, movementDto.amount);
            HttpController.sendResponse(httpExchange, this.gson.toJson(new MovementSuccessDto("Deposit completed successfully")), OK);
        } catch (AccountNotFound e) {
            HttpController.sendResponse(httpExchange, "Account not found", NOT_FOUND);
        }
    }

    private void transfer(HttpExchange httpExchange, String request) {
        if (!httpExchange.getRequestMethod().equals("POST")) {
            return;
        }

        MovementDto movementDto = this.gson.fromJson(request, MovementDto.class);
        try {
            this.movementService.transfer(movementDto.origin, movementDto.destination, movementDto.amount);
            HttpController.sendResponse(httpExchange, this.gson.toJson(new MovementSuccessDto("Deposit completed successfully")), OK);
        } catch (AccountNotFound e) {
            HttpController.sendResponse(httpExchange, "Account not found", NOT_FOUND);
        }
    }
}
