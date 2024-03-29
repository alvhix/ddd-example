package account.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "ACCOUNT")
public final class AccountEntity {
    @Id
    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "balance")
    private Double balance;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private OwnerEntity owner;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private Set<MovementEntity> movements;

    public AccountEntity() {
    }

    public AccountEntity(UUID uuid, Double balance) {
        this.uuid = uuid;
        this.balance = balance;
    }

    public UUID uuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Double balance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public OwnerEntity owner() {
        return owner;
    }

    public void setOwner(OwnerEntity owner) {
        this.owner = owner;
    }

    public Set<MovementEntity> movements() {
        return movements;
    }

    public void setMovements(Set<MovementEntity> movements) {
        this.movements = movements;
    }
}

