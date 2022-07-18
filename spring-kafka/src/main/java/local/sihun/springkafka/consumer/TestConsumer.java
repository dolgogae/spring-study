package local.sihun.springkafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

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


}
