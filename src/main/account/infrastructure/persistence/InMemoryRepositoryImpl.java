package main.account.infrastructure.persistence;

import main.account.domain.Account;
import main.account.domain.AccountRepository;
import main.account.infrastructure.persistence.vo.AccountVO;
import main.shared.infrastructure.persistence.Mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public final class InMemoryRepositoryImpl implements AccountRepository {
    private final List<AccountVO> accountVOs;
    private final Mapping mapping;


    public InMemoryRepositoryImpl() {
        this.accountVOs = new ArrayList<>();
        this.mapping = new Mapping();
    }

    public List<Account> all() {
        return mapping.mapAccountsToDomain(accountVOs);
    }

    @Override
    public Optional<Account> get(UUID uuid) {
        return this.accountVOs.stream().filter(accountVO -> accountVO.uuid()
                .equals(uuid)).findFirst().map(mapping::mapToDomain);
    }

    @Override
    public void save(Account account) {
        this.accountVOs.add(mapping.mapToPersistence(account));
    }

    @Override
    public void save(List<Account> accounts) {
        accounts.forEach(this::save);
    }

    @Override
    public void update(Account account) {
        Optional<Integer> indexOpt = Optional.of(accountVOs.stream()
                .map(AccountVO::uuid)
                .collect(Collectors.toList())
                .indexOf(account.uuid()));

        indexOpt.ifPresent(index -> accountVOs.set(index, mapping.mapToPersistence(account)));
    }
}
