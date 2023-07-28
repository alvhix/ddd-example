package main.account.application;

import main.account.domain.*;

public final class TransactionService {
    private final AccountRepository accountRepository;
    private final AccountFinder accountFinder;

    public TransactionService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        this.accountFinder = new AccountFinder(accountRepository);
    }

    public void transfer(String origin, String destination, Double amount) throws AccountNotFound {
        Account originAccount = this.accountFinder.find(origin);
        Account destinationAccount = this.accountFinder.find(destination);

        originAccount.addMovement(amount, MovementType.EXPENSE);
        destinationAccount.addMovement(amount, MovementType.INCOME);

        this.accountRepository.update(originAccount);
        this.accountRepository.update(destinationAccount);
    }

    public void deposit(String accountUuid, Double amount) throws AccountNotFound {
        Account account = this.accountFinder.find(accountUuid);

        account.addMovement(amount, MovementType.INCOME);

        this.accountRepository.update(account);
    }

    public void withdraw(String accountUuid, Double amount) throws AccountNotFound {
        Account account = this.accountFinder.find(accountUuid);

        account.addMovement(amount, MovementType.EXPENSE);

        this.accountRepository.update(account);
    }
}
