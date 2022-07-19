package local.sihun.springkafka.configuration;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.core.RoutingKafkaTemplate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Configuration
public class RoutingKafkaTemplateConfiguration {

    @Bean
    public RoutingKafkaTemplate routingKafkaTemplate(){
        return new RoutingKafkaTemplate(factories());
    }

    private Map<Pattern, ProducerFactory<Object, Object>> factories() {
        Map<Pattern, ProducerFactory<Object, Object>> factoryMap = new LinkedHashMap<>();
        factoryMap.put(Pattern.compile("test.bytes"), bytesProducerFactory());
        factoryMap.put(Pattern.compile(".*"), defaultProducerFactory());

        return factoryMap;
    }

    private ProducerFactory<Object, Object> bytesProducerFactory() {
        Map<String, Object> props = producerProps();
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    private ProducerFactory<Object, Object> defaultProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerProps());
    }

    private Map<String, Object> producerProps() {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return props;
    }
}
