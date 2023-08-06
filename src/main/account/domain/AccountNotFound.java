package main.account.domain;

import java.util.UUID;

public class AccountNotFound extends Exception {
    public AccountNotFound(UUID uuid) {
        super(String.valueOf(uuid));
    }
}
