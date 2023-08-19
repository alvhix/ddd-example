package account.domain;

import java.util.UUID;

public final class AccountNotFound extends Exception {
    public AccountNotFound(UUID uuid) {
        super(String.valueOf(uuid));
    }
}
