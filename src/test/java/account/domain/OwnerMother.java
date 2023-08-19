package account.domain;

import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

public final class OwnerMother {
    public static Owner create() {
        return new Owner(UUID.randomUUID(),
                RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphabetic(20),
                RandomStringUtils.randomAlphabetic(9));
    }
}
