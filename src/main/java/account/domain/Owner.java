package account.domain;

import java.util.Objects;
import java.util.UUID;

public final class Owner {
    private final UUID uuid;
    private final String name;
    private final String firstName;
    private final String nif;

    public Owner(UUID uuid, String name, String firstName, String nif) {
        this.uuid = uuid;
        this.name = name;
        this.firstName = firstName;
        this.nif = nif;
    }

    public static Owner create(String name, String firstName, String nif) {
        return new Owner(UUID.randomUUID(), name, firstName, nif);
    }

    public UUID uuid() {
        return uuid;
    }

    public String name() {
        return name;
    }

    public String firstName() {
        return firstName;
    }

    public String nif() {
        return nif;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Owner)) return false;
        Owner owner = (Owner) o;
        return Objects.equals(uuid, owner.uuid) && Objects.equals(name, owner.name) && Objects.equals(firstName, owner.firstName) && Objects.equals(nif, owner.nif);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name, firstName, nif);
    }
}
