package main.shared.infrastructure.bus;

import com.google.common.eventbus.EventBus;
import main.shared.application.DomainEventSubscriber;
import main.shared.domain.DomainEvent;

import java.util.List;

public class GuavaEventBus implements main.shared.domain.EventBus {
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
