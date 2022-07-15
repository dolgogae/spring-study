package local.sihun.springkafka.configuration;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfiguration {

    @Bean
    public AdminClient adminClient(KafkaAdmin kafkaAdmin){
        return AdminClient.create(kafkaAdmin.getConfigurationProperties());
    }


    @Bean
    public KafkaAdmin.NewTopics newTopics(){
        return new KafkaAdmin.NewTopics(
                TopicBuilder.name("test.topic1")
                        .partitions(3)
                        .replicas(3)
                        .config(TopicConfig.RETENTION_MS_CONFIG, String.valueOf(1000*60*60))
                        .build(),

                TopicBuilder.name("test.topic3")
                        .partitions(3)
                        .replicas(3)
                        .config(TopicConfig.RETENTION_MS_CONFIG, String.valueOf(1000*60*60))
                        .build(),
                TopicBuilder.name("test.bytes").build(),
                TopicBuilder.name("test.topic.request").build(),
                TopicBuilder.name("test.topic.replies").build()
        );
    }
}
