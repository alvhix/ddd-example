package main.account.domain;

public class AccountNotFound extends Exception {
    public AccountNotFound(String uuid) {
        super(uuid);
    }
}
