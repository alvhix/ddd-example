package shared.infrastructure.bus;

import com.google.common.eventbus.EventBus;

import shared.application.DomainEventSubscriber;

import shared.domain.DomainEvent;
import java.util.List;

public final class GuavaEventBus implements shared.domain.EventBus {
    private final EventBus eventBus;

    public GuavaEventBus() {
        this.eventBus = new EventBus();
    }

    @Override
    public void publish(List<DomainEvent> events) {
        this.eventBus.post(events);
    }

    public void register(DomainEventSubscriber<DomainEvent> domainEventSubscriber) {
        this.eventBus.register(domainEventSubscriber);
    }
}
