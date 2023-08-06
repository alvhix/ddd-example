package main.account.infrastructure.persistence.vo;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ACCOUNT")
public class AccountVO {
    @Id
    @Column(name = "uuid")
    private UUID uuid;
    @OneToOne
    @JoinColumn(name = "uuid")
    private OwnerVO owner;

    @OneToMany(mappedBy = "uuid")
    private List<MovementVO> movements;

    @Column(name = "balance")
    private Double balance;

    public AccountVO(UUID uuid, OwnerVO owner, List<MovementVO> movements, Double balance) {
        this.uuid = uuid;
        this.owner = owner;
        this.movements = movements;
        this.balance = balance;
    }

    public UUID uuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public OwnerVO owner() {
        return owner;
    }

    public void setOwner(OwnerVO owner) {
        this.owner = owner;
    }

    public List<MovementVO> movements() {
        return movements;
    }

    public void setMovements(List<MovementVO> movements) {
        this.movements = movements;
    }

    public Double balance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}

