package account.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "MOVEMENT")
public class MovementEntity {
    @Id
    @Column(name = "uuid")
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "account_uuid")
    private AccountEntity account;
    @Column(name = "amount")
    private Double amount;
    @Column(name = "type")
    private String type;

    public MovementEntity() {
    }

    public MovementEntity(UUID uuid, Double amount, String type) {
        this.uuid = uuid;
        this.amount = amount;
        this.type = type;
    }

    public UUID uuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public AccountEntity account() {
        return account;
    }

    public void setAccount(AccountEntity account) {
        this.account = account;
    }

    public Double amount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String type() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

