package io.xgeeks.examples.spring.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NameService {

    private final NameCounter counter;

    private final KafkaTemplate<String, io.xgeeks.examples.spring.kafka.User> template;

    @Autowired
    CaptainTransactionKafkaPublisher captainTransactionKafkaPublisher;

    public NameService(NameCounter counter, KafkaTemplate<String, io.xgeeks.examples.spring.kafka.User> template) {
        this.counter = counter;
        this.template = template;
    }

    public List<NameStatus> findAll() {
        return counter.getValues()
                .map(NameStatus::of)
                .collect(Collectors.toUnmodifiableList());
    }

    public NameStatus findByName(String name) {
        return new NameStatus(name, counter.get(name));
    }

    public void decrement(String name) {

    }

    public void increment(String name) {
        captainTransactionKafkaPublisher.publishCreateCaptainTransactionEvent(new io.xgeeks.examples.spring.kafka.User("boom", 2,"blue"));
    }
}
