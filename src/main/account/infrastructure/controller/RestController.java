package main.account.infrastructure.controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.account.application.AccountSearcher;
import main.account.application.TransactionService;
import main.account.domain.*;
import main.account.infrastructure.dto.TransactionDto;
import main.account.infrastructure.dto.TransactionSuccessDto;
import main.account.infrastructure.persistence.InMemoryRepositoryImpl;
import main.shared.infrastructure.HttpController;

import java.util.*;

public final class RestController implements HttpHandler {
    private final static Integer OK = 200;
    private final static Integer NOT_FOUND = 404;
    private final AccountSearcher accountSearcher;
    private final TransactionService transactionService;
    private final Gson gson;

    public RestController() {
        List<Account> accounts = new ArrayList<>(List.of(
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
                                new Movement(500.00, MovementType.EXPENSE))
                        )
                )
        ));
        AccountRepository accountRepository = new InMemoryRepositoryImpl(accounts);
        this.accountSearcher = new AccountSearcher(accountRepository);
        this.transactionService = new TransactionService(accountRepository);
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

        HttpController.sendResponse(httpExchange, this.gson.toJson(this.accountSearcher.getAllAccounts()), OK);
    }

    private void getAllMovements(HttpExchange httpExchange, String uuid) {
        if (!httpExchange.getRequestMethod().equals("GET")) {
            return;
        }

        try {
            HttpController.sendResponse(httpExchange, this.gson.toJson(this.accountSearcher.getAllMovements(uuid)), OK);
        } catch (AccountNotFound e) {
            HttpController.sendResponse(httpExchange, "Account not found", NOT_FOUND);
        }
    }

    private void getAccount(HttpExchange httpExchange, String uuid) {
        if (!httpExchange.getRequestMethod().equals("GET")) {
            return;
        }

        try {
            Account account = this.accountSearcher.getAccount(uuid);
            HttpController.sendResponse(httpExchange, this.gson.toJson(account), OK);
        } catch (AccountNotFound e) {
            HttpController.sendResponse(httpExchange, "Account not found", NOT_FOUND);
        }
    }

    private void deposit(HttpExchange httpExchange, String request) {
        if (!httpExchange.getRequestMethod().equals("POST")) {
            return;
        }

        TransactionDto transactionDto = this.gson.fromJson(request, TransactionDto.class);
        try {
            this.transactionService.deposit(transactionDto.origin, transactionDto.amount);
            HttpController.sendResponse(httpExchange, this.gson.toJson(new TransactionSuccessDto("Deposit completed successfully")), OK);
        } catch (AccountNotFound e) {
            HttpController.sendResponse(httpExchange, "Account not found", NOT_FOUND);
        }
    }

    private void withdraw(HttpExchange httpExchange, String request) {
        if (!httpExchange.getRequestMethod().equals("POST")) {
            return;
        }

        TransactionDto transactionDto = this.gson.fromJson(request, TransactionDto.class);
        try {
            this.transactionService.withdraw(transactionDto.origin, transactionDto.amount);
            HttpController.sendResponse(httpExchange, this.gson.toJson(new TransactionSuccessDto("Deposit completed successfully")), OK);
        } catch (AccountNotFound e) {
            HttpController.sendResponse(httpExchange, "Account not found", NOT_FOUND);
        }
    }

    private void transfer(HttpExchange httpExchange, String request) {
        if (!httpExchange.getRequestMethod().equals("POST")) {
            return;
        }

        TransactionDto transactionDto = this.gson.fromJson(request, TransactionDto.class);
        try {
            this.transactionService.transfer(transactionDto.origin, transactionDto.destination, transactionDto.amount);
            HttpController.sendResponse(httpExchange, this.gson.toJson(new TransactionSuccessDto("Deposit completed successfully")), OK);
        } catch (AccountNotFound e) {
            HttpController.sendResponse(httpExchange, "Account not found", NOT_FOUND);
        }
    }
}
