package account.infrastructure.persistence;

import account.domain.Account;
import account.domain.AccountRepository;
import account.infrastructure.persistence.entity.AccountEntity;
import account.infrastructure.persistence.entity.MovementEntity;
import account.infrastructure.persistence.entity.OwnerEntity;
import org.hibernate.Session;
import shared.infrastructure.persistence.Mapper;
import shared.infrastructure.persistence.MySql;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class MySqlAccountRepositoryImpl extends MySql implements AccountRepository {

    @Override
    public List<Account> all() {
        Session session = sessionFactory.openSession();
        List<AccountEntity> accounts = session.createQuery("FROM ACCOUNT", AccountEntity.class).list();
        session.close();

        return accounts.stream().map(Mapper::mapToDomain).collect(Collectors.toList());
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

        List<MovementEntity> movements = Mapper.mapToEntity(account.movements());
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

        List<MovementEntity> movements = Mapper.mapToEntity(account.movements());
        movements.forEach(x -> x.setAccount(accountEntity));

        accountEntity.setOwner(owner);
        accountEntity.setMovements(movements);

        session.merge(accountEntity);
        session.getTransaction().commit();
        session.close();
    }
}
