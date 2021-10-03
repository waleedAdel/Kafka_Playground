package io.xgeeks.examples.spring.kafka;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
public class CaptainTransactionKafkaPublisher {
    private final KafkaTemplate<String, io.xgeeks.examples.spring.kafka.User> kafkaTemplate;
    private final String createCaptainTransactionTopic;

    public CaptainTransactionKafkaPublisher(
            @Qualifier("createCaptainTransactionKafkaTemplate") KafkaTemplate<String, io.xgeeks.examples.spring.kafka.User> kafkaTemplate,
            @Value("${kafka.topic.create-captain-transaction.name}") String createCaptainTransactionTopic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.createCaptainTransactionTopic = createCaptainTransactionTopic;
    }

    public void publishCreateCaptainTransactionEvent(io.xgeeks.examples.spring.kafka.User eventDto) {
        ListenableFuture<SendResult<String, io.xgeeks.examples.spring.kafka.User>> future =
                kafkaTemplate.send(createCaptainTransactionTopic, eventDto);

        future.addCallback(new ListenableFutureCallback<SendResult<String, io.xgeeks.examples.spring.kafka.User>>() {
            @Override
            public void onSuccess(SendResult<String, io.xgeeks.examples.spring.kafka.User> result) {
                System.out.println(
                        "CreateCaptainTransaction Kafka event {} published to topic {} with offset {}"
                );
            }
            @Override
            public void onFailure(Throwable exception) {
                System.out.println(
                        "error" + exception
                );
            }
        });
    }
}
