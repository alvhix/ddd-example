package main.account.domain;

public class AccountUuid {
    private final String value;

    public AccountUuid(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
