package main.account.infrastructure.persistence.mapper.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "OWNER")
public class OwnerEntity {
    @Id
    @Column(name = "uuid")
    private UUID uuid;
    @Column(name = "name")
    private String name;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "nif")
    private String nif;
    @OneToOne
    @JoinColumn(name = "account_uuid")
    private AccountEntity account;

    public OwnerEntity() {
    }

    public OwnerEntity(UUID uuid, String name, String firstName, String nif) {
        this.uuid = uuid;
        this.name = name;
        this.firstName = firstName;
        this.nif = nif;
    }

    public UUID uuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String firstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String nif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public AccountEntity account() {
        return account;
    }

    public void setAccount(AccountEntity account) {
        this.account = account;
    }
}

