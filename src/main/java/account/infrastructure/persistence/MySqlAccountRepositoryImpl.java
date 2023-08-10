package account.infrastructure.persistence;

import account.domain.*;
import account.infrastructure.persistence.mapper.entity.AccountEntity;
import account.infrastructure.persistence.mapper.entity.MovementEntity;
import account.infrastructure.persistence.mapper.entity.OwnerEntity;
import shared.infrastructure.persistence.Mapper;
import shared.infrastructure.persistence.MySqlConfig;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class MySqlAccountRepositoryImpl extends MySqlConfig implements AccountRepository {

    @Override
    public List<Account> all() {
        List<AccountEntity> accounts = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            accounts = session.createQuery("FROM ACCOUNT", AccountEntity.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accounts.stream().map(Mapper::mapToDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Account> get(UUID uuid) {
        Account account = null;
        try (Session session = sessionFactory.openSession()) {
            AccountEntity accountEntity = session
                    .createQuery("FROM AccountEntity WHERE uuid = :uuid", AccountEntity.class)
                    .setParameter("uuid", uuid).getSingleResult();
            account = Mapper.mapToDomain(accountEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(account);
    }

    @Override
    public void save(Account account) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            AccountEntity accountEntity = Mapper.mapToEntity(account);

            OwnerEntity owner = Mapper.mapToEntity(account.owner());
            owner.setAccount(accountEntity);

            List<MovementEntity> movements = Mapper.mapToEntity(account.movements());
            movements.forEach(x -> x.setAccount(accountEntity));

            accountEntity.setOwner(owner);
            accountEntity.setMovements(movements);

            session.persist(accountEntity);
            session.flush();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(List<Account> accounts) {
        accounts.forEach(this::save);
    }

    @Override
    public void update(Account account) {
        try (Session session = sessionFactory.openSession()) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
