package shared.application;

import shared.domain.DomainEvent;

public interface DomainEventSubscriber<EventType extends DomainEvent> {
    Class<EventType> subscribedTo();

    void consume(EventType event);
}
