package main.account.infrastructure.persistence;

import main.account.domain.Account;
import main.account.domain.AccountRepository;
import main.account.infrastructure.persistence.vo.AccountVO;
import main.shared.infrastructure.persistence.Mapping;
import main.shared.infrastructure.persistence.MySqlConfig;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MySqlRepositoryImpl extends MySqlConfig implements AccountRepository {
    private final Mapping mapping;

    public MySqlRepositoryImpl() {
        this.mapping = new Mapping();
    }

    @Override
    public List<Account> all() {
        Session session = super.sessionFactory.openSession();
        List<AccountVO> accounts = session.createQuery("FROM ACCOUNT", AccountVO.class).list();
        super.sessionFactory.close();

        return mapping.mapAccountsToDomain(accounts);
    }

    @Override
    public Optional<Account> get(UUID uuid) {
        Session session = super.sessionFactory.openSession();
        Optional<AccountVO> accountVO = session
                .createQuery("FROM ACCOUNT WHERE uuid = :uuid", AccountVO.class)
                .setParameter("uuid", uuid).uniqueResultOptional();
        super.sessionFactory.close();

        return accountVO.map(mapping::mapToDomain);
    }

    @Override
    public void save(Account account) {
        Session session = super.sessionFactory.openSession();
        session.persist(mapping.mapToPersistence(account));
        super.sessionFactory.close();
    }

    @Override
    public void save(List<Account> accounts) {
        accounts.forEach(this::save);
    }

    @Override
    public void update(Account account) {
        Session session = super.sessionFactory.openSession();
        session.merge(mapping.mapToPersistence(account));

        super.sessionFactory.close();
    }
}
