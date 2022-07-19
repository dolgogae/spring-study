package local.sihun.springkafka.consumer;

import local.sihun.springkafka.model.Animal;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Date;

@Service
public class TestConsumer {

//    @KafkaListener(id = "test.id", topics = "test.topic3")
//    public void listener(String message){
//        System.out.println(message);
//    }
//
//    @KafkaListener(id = "test.bytes.id", topics = "test.bytes")
//    public void byteListener(String message){
//        System.out.println(message);
//    }
//
//    @KafkaListener(id = "test.topic.request.id", topics = "test.topic.request")
//    @SendTo
//    public String requestListener(String message){
//        System.out.println(message);
//        return "Pong test";
//    }

    // concurrency: thread 갯수
    // clientIdPrefix: client id를 지정할 수 있다.
    @KafkaListener(id = "test4-listener-id", topics = "test.topic4")
//            ,concurrency = "2", clientIdPrefix = "listener_id")
    public void listen(String message,
                       ConsumerRecordMetadata metadata,
                       @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp,
                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                       @Header(KafkaHeaders.OFFSET) long offset){
        System.out.println("Listener message = "+message);
        System.out.println("offset = "+metadata.offset());

        System.out.println("timestamp: "+ new Date(timestamp));
        System.out.println("partition id: "+partition);
    }

    @KafkaListener(id = "test4-animal-listener-id", topics = "test.topic.animal", containerFactory = "kafkaJsonContainerFactory")
    public void listenAnimal(@Valid Animal animal){
        System.out.println("animal: " + animal);
    }

}
