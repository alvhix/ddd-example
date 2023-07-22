package application;

import domain.AccountRepository;

public final class TransferenceService {
    private final AccountRepository accountRepository;

    public TransferenceService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void transfer(String origin, String destination, Double quantity) {
        this.accountRepository.transfer(origin, destination, quantity);
    }
}
