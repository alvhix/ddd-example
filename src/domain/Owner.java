package domain;

public class Owner {
    private final String name;
    private final String firstName;
    private final String nif;

    public Owner(String name, String firstName, String nif) {
        this.name = name;
        this.firstName = firstName;
        this.nif = nif;
    }

    public String getName() {
        return name;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getNif() {
        return nif;
    }
}
