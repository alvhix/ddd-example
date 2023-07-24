package main.account.infrastructure.controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.account.application.AccountSearcher;
import main.account.application.TransferenceService;
import main.account.domain.*;
import main.account.infrastructure.dto.TransactionDto;
import main.account.infrastructure.persistence.InMemoryRepositoryImpl;
import main.shared.infrastructure.HttpController;


import java.io.IOException;
import java.util.*;

public final class RestController implements HttpHandler {
    private final AccountSearcher accountSearcher;
    private final TransferenceService transferenceService;
    private final Gson gson;

    public RestController() {
        List<Account> accounts = List.of(
                new Account("ff6f9c90-bbb7-409c-87d2-04277f85d111",
                        new Owner("William", "Mote", "43957942C"),
                        new ArrayList<>(List.of(
                                new Movement(1000.00, MovementType.INCOME),
                                new Movement(200.00, MovementType.INCOME))
                        )
                ),
                new Account("249c9b83-4912-4719-9d5d-a27a3b4c4a8c",
                        new Owner("Maria", "Garcia", "22392403V"),
                        new ArrayList<>(List.of(
                                new Movement(1000.00, MovementType.INCOME),
                                new Movement(300.00, MovementType.EXPENSE))
                        )
                )
        );
        AccountRepository accountRepository = new InMemoryRepositoryImpl(accounts);
        this.accountSearcher = new AccountSearcher(accountRepository);
        this.transferenceService = new TransferenceService(accountRepository);
        this.gson = new Gson();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String action = httpExchange.getRequestURI().getPath().split("/")[2];
        Map<String, String> query = HttpController.queryToMap(httpExchange.getRequestURI().getQuery());
        switch (action) {
            case "getAll":
                if (!httpExchange.getRequestMethod().equals("GET")) {
                    return;
                }
                HttpController.sendResponse(httpExchange, getAllAccounts());
                break;
            case "getMovements":
                if (!httpExchange.getRequestMethod().equals("GET")) {
                    return;
                }
                HttpController.sendResponse(httpExchange, getAllMovements(query.get("uuid")));
                break;
            case "deposit":
                if (!httpExchange.getRequestMethod().equals("POST")) {
                    return;
                }
                HttpController.sendResponse(httpExchange, deposit(HttpController.getRequestBody(httpExchange)));
                break;
            case "withdraw":
                if (!httpExchange.getRequestMethod().equals("POST")) {
                    return;
                }
                HttpController.sendResponse(httpExchange, withdraw(HttpController.getRequestBody(httpExchange)));
                break;
            case "transfer":
                if (!httpExchange.getRequestMethod().equals("POST")) {
                    return;
                }
                transfer(HttpController.getRequestBody(httpExchange));
                HttpController.sendResponse(httpExchange, "Ok");
        }
    }

    private String getAllAccounts() {
        return this.gson.toJson(this.accountSearcher.getAllAccounts());
    }

    private String getAllMovements(String uuid) {
        return this.gson.toJson(this.accountSearcher.getAllMovements(uuid));
    }

    private String deposit(String request) {
        TransactionDto transactionDto = this.gson.fromJson(request, TransactionDto.class);
        return this.transferenceService.deposit(transactionDto.origin, transactionDto.amount).toString();
    }

    private String withdraw(String request) {
        TransactionDto transactionDto = this.gson.fromJson(request, TransactionDto.class);
        return this.transferenceService.withdraw(transactionDto.origin, transactionDto.amount).toString();
    }

    private void transfer(String request) {
        TransactionDto transactionDto = this.gson.fromJson(request, TransactionDto.class);
        this.transferenceService.transfer(transactionDto.origin, transactionDto.destination, transactionDto.amount);
    }
}
