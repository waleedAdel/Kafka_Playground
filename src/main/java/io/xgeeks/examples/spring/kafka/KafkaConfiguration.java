package io.xgeeks.examples.spring.kafka;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Profile("!test")
@EnableKafka
@Configuration
public class KafkaConfiguration {

    private static final String SASL_MECHANISM = "sasl.mechanism";
    private static final String SASL_JASS_CONFIG = "sasl.jaas.config";
    private static final String SECURITY_PROTOCOL = "security.protocol";

    @Value("${spring.kafka.properties.schema-registry-url}")
    String registryUrl;

    @Value("${spring.kafka.bootstrap-servers}")
    String kafkaCluster;


    @Bean
    public ProducerFactory<String, io.xgeeks.examples.spring.kafka.User> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, registryUrl);
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaCluster);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean("createCaptainTransactionKafkaTemplate")
    public KafkaTemplate<String, io.xgeeks.examples.spring.kafka.User> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConsumerFactory<String, io.xgeeks.examples.spring.kafka.User> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, registryUrl);
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaCluster);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
        configProps.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true");
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, io.xgeeks.examples.spring.kafka.User> greetingKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, io.xgeeks.examples.spring.kafka.User> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}