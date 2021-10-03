package io.xgeeks.examples.spring.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class NameConsumer {

    private static final Logger LOGGER = Logger.getLogger(NameConsumer.class.getName());

    private final NameCounter counter;

    public NameConsumer(NameCounter counter) {
        this.counter = counter;
    }


    @KafkaListener(
            topics = "topic_waleed",
            containerFactory = "greetingKafkaListenerContainerFactory",
        groupId = "increment")
    public void increment(io.xgeeks.examples.spring.kafka.User user) {
        LOGGER.info("Increment listener to the name" + user.getName() + " " + user.getFavoriteColor());
    }

}
