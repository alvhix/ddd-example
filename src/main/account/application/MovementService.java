package main.account.application;

import main.account.domain.*;
import main.shared.domain.EventBus;

public final class MovementService {
    private final AccountRepository accountRepository;
    private final AccountFinder accountFinder;
    private final EventBus eventBus;

    public MovementService(AccountRepository accountRepository, EventBus eventBus) {
        this.accountRepository = accountRepository;
        this.accountFinder = new AccountFinder(accountRepository);
        this.eventBus = eventBus;
    }

    public void transfer(String origin, String destination, Double amount) throws AccountNotFound {
        Account originAccount = this.accountFinder.find(origin);
        Account destinationAccount = this.accountFinder.find(destination);

        originAccount.addMovement(amount, MovementType.EXPENSE);
        destinationAccount.addMovement(amount, MovementType.INCOME);

        this.accountRepository.update(originAccount);
        this.accountRepository.update(destinationAccount);

        this.eventBus.publish(originAccount.pullDomainEvents());
        this.eventBus.publish(destinationAccount.pullDomainEvents());
    }

    public void deposit(String accountUuid, Double amount) throws AccountNotFound {
        Account account = this.accountFinder.find(accountUuid);

        account.addMovement(amount, MovementType.INCOME);

        this.accountRepository.update(account);

        this.eventBus.publish(account.pullDomainEvents());
    }

    public void withdraw(String accountUuid, Double amount) throws AccountNotFound {
        Account account = this.accountFinder.find(accountUuid);

        account.addMovement(amount, MovementType.EXPENSE);

        this.accountRepository.update(account);

        this.eventBus.publish(account.pullDomainEvents());
    }
}
