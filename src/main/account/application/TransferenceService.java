package main.account.application;

import main.account.domain.AccountRepository;

public final class TransferenceService {
    private final AccountRepository accountRepository;

    public TransferenceService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void transfer(String origin, String destination, Double amount) {
        this.accountRepository.transfer(origin, destination, amount);
    }

    public Double deposit(String account, Double amount) {
        return this.accountRepository.deposit(account, amount);
    }

    public Double withdraw(String account, Double amount) {
        return this.accountRepository.withdraw(account, amount);
    }
}
