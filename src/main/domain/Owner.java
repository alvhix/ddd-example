package main.domain;

public class Owner {
    private final String name;
    private final String firstName;
    private final String nif;

    public Owner(String name, String firstName, String nif) {
        this.name = name;
        this.firstName = firstName;
        this.nif = nif;
    }

    @Override
    public String toString() {
        return "Owner{" +
                "name='" + name + '\'' +
                ", firstName='" + firstName + '\'' +
                ", nif='" + nif + '\'' +
                '}';
    }
}
