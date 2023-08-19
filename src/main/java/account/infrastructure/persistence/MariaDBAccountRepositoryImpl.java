package account.infrastructure.persistence;

import account.domain.Account;
import account.domain.AccountRepository;
import account.infrastructure.persistence.entity.AccountEntity;
import account.infrastructure.persistence.entity.MovementEntity;
import account.infrastructure.persistence.entity.OwnerEntity;

import org.hibernate.Session;
import org.testcontainers.containers.MariaDBContainer;
import shared.infrastructure.persistence.Mapper;
import shared.infrastructure.persistence.MariaDB;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public final class MariaDBAccountRepositoryImpl extends MariaDB implements AccountRepository {

    public MariaDBAccountRepositoryImpl() {
        super();
    }

    public MariaDBAccountRepositoryImpl(MariaDBContainer<?> mariaDBContainer) {
        super(mariaDBContainer);
    }

    @Override
    public List<Account> all() {
        Session session = sessionFactory.openSession();
        List<AccountEntity> accountEntityList = session.createQuery("FROM AccountEntity", AccountEntity.class).list();
        List<Account> accounts = accountEntityList.stream().map(Mapper::mapToDomain).collect(Collectors.toList());
        session.close();

        return accounts;
    }

    @Override
    public Optional<Account> get(UUID uuid) {
        Session session = sessionFactory.openSession();
        AccountEntity accountEntity = session
                .createQuery("FROM AccountEntity WHERE uuid = :uuid", AccountEntity.class)
                .setParameter("uuid", uuid).getSingleResult();
        Account account = Mapper.mapToDomain(accountEntity);
        session.close();

        return Optional.of(account);
    }

    @Override
    public void save(Account account) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        AccountEntity accountEntity = Mapper.mapToEntity(account);

        OwnerEntity owner = Mapper.mapToEntity(account.owner());
        owner.setAccount(accountEntity);

        Set<MovementEntity> movements = Mapper.mapToEntity(account.movements());
        movements.forEach(x -> x.setAccount(accountEntity));

        accountEntity.setOwner(owner);
        accountEntity.setMovements(movements);

        session.persist(accountEntity);
        session.getTransaction().commit();

        session.close();
    }

    @Override
    public void save(List<Account> accounts) {
        accounts.forEach(this::save);
    }

    @Override
    public void update(Account account) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        AccountEntity accountEntity = Mapper.mapToEntity(account);

        OwnerEntity owner = Mapper.mapToEntity(account.owner());
        owner.setAccount(accountEntity);

        Set<MovementEntity> movements = Mapper.mapToEntity(account.movements());
        movements.forEach(x -> x.setAccount(accountEntity));

        accountEntity.setOwner(owner);
        accountEntity.setMovements(movements);

        session.merge(accountEntity);
        session.getTransaction().commit();

        session.close();
    }
}
