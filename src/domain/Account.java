package domain;

public class Account {
    private final String uuid;
    private final Owner owner;
    private final Integer balance;
    private final Movement[] movements;

    public Account(String uuid, Owner owner, Integer balance, Movement[] movements) {
        this.uuid = uuid;
        this.owner = owner;
        this.balance = balance;
        this.movements = movements;
    }

    public String getUuid() {
        return uuid;
    }

    public Owner getOwner() {
        return owner;
    }

    public Integer getBalance() {
        return balance;
    }

    public Movement[] getMovements() {
        return movements;
    }
}
