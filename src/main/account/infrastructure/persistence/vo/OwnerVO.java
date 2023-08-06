package main.account.infrastructure.persistence.vo;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "OWNER")
public class OwnerVO {
    @Id
    @Column(name = "uuid")
    private UUID uuid;
    @Column(name = "name")
    private String name;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "nif")
    private String nif;

    @OneToOne(mappedBy = "uuid")
    private AccountVO account;

    public OwnerVO(UUID uuid, String name, String firstName, String nif) {
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

    public AccountVO account() {
        return account;
    }

    public void setAccount(AccountVO account) {
        this.account = account;
    }
}

