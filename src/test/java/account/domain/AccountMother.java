package account.domain;

import java.util.UUID;

public final class AccountMother {
    public static Account create() {
        return new Account(UUID.randomUUID(), OwnerMother.create(), MovementMother.create());
    }
}
