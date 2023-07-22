package main.application;

import main.domain.AccountRepository;

public final class TransferenceService {
    private final AccountRepository accountRepository;

    public TransferenceService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void transfer(String origin, String destination, Double quantity) {
        this.accountRepository.transfer(origin, destination, quantity);
    }

    public Double deposit(String account, Double quantity) {
        return this.accountRepository.deposit(account, quantity);
    }

    public Double withdraw(String account, Double quantity) {
        return this.accountRepository.withdraw(account, quantity);
    }
}
