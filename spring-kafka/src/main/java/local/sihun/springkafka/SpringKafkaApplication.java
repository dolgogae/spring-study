package local.sihun.springkafka;

import local.sihun.springkafka.model.Animal;
import local.sihun.springkafka.producer.TestProducer;
import org.apache.kafka.clients.KafkaClient;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.clients.admin.TopicListing;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

@SpringBootApplication
public class SpringKafkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringKafkaApplication.class, args);
	}

	@Bean
	public ApplicationRunner runner(TestProducer testProducer){
		return args -> {
			//			testProducer.async("test.topic4", "hello, test4 Listener");
			testProducer.async("test.topic.animal", new Animal("puppy", 5));
		};
	}
//	@Bean
//	public ApplicationRunner runner(KafkaTemplate<String, String> kafkaTemplate){
//		return args -> {
//			kafkaTemplate.send("test.topic1", "hello, kafka");
//		};

//	}
//	@Bean
//	public ApplicationRunner runner(AdminClient adminClient){
//		return args -> {
//			Map<String, TopicListing> topics = adminClient.listTopics().namesToListings().get();
//			for(String topicName: topics.keySet()){
//				TopicListing topicListing = topics.get(topicName);
//				System.out.println(topicListing);
//
//				Map<String, TopicDescription> description = adminClient.describeTopics(Collections.singleton(topicName)).all().get();
//				System.out.println(description);
//
//				if(!topicListing.isInternal()) {
//					adminClient.deleteTopics(Collections.singleton(topicName));
//				}
//			}
//		};

//	}
//
//	@Bean
//	public ApplicationRunner runner(TestProducer testProducer){
//		return args -> {
//			testProducer.async("test.topic1", "hello kafka producer async" );
//			testProducer.sync("test.topic1", "hello kafka producer sync");
//			testProducer.routingSend("test.topic3", "hello kafka string send");
//			testProducer.byteRoutingSend("test.bytes", "hello kafka byte send".getBytes(StandardCharsets.UTF_8));
//			testProducer.replyingSend("test.topic.request", "Ping test");
//		};
//	}
//
//	@Bean
//	public ApplicationRunner runner(TestProducer testProducer,
//									KafkaMessageListenerContainer<String, String> kafkaMessageListenerContainer){
//		return args -> {
//			testProducer.async("test.topic4", "hello, kafka container");
//			kafkaMessageListenerContainer.start();
//			Thread.sleep(1_000L);
//
//			// 멈추기
//			System.out.println("--- pause ---");
//			kafkaMessageListenerContainer.pause();
//			Thread.sleep(5_000L);
//
//			testProducer.async("test.topic4", "hello, secondly kafka container");
//
//			// 다시 읽기
//			kafkaMessageListenerContainer.resume();
//			Thread.sleep(1_000L);
//
//			// 중단
//			System.out.println("--- stop ---");
//			kafkaMessageListenerContainer.stop();
//		};

//	}
}
