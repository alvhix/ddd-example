package account.domain;

import shared.domain.DomainEvent;
import shared.domain.EventBus;

import java.util.ArrayList;
import java.util.List;

public class FakeEventBus implements EventBus {
    private final List<DomainEvent> events;

    public FakeEventBus() {
        this.events = new ArrayList<>();
    }

    @Override
    public void publish(List<DomainEvent> events) {
        this.events.addAll(events);
    }

    public List<DomainEvent> getEvents() {
        return this.events;
    }
}
