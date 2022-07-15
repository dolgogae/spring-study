package local.sihun.springkafka.producer;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.RoutingKafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
public class TestProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final RoutingKafkaTemplate routingKafkaTemplate;
    private final ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;

    public void async(String topic, String message){
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);
        future.addCallback(new KafkaSendCallback<>(){

            @Override
            public void onSuccess(SendResult<String, String> result) {
                System.out.println("Seccess to send message.");
            }

            @Override
            public void onFailure(KafkaProducerException ex) {
                System.out.println("Fail to send message. record="+ex.getFailedProducerRecord());
            }
        });
    }

    public void sync(String topic, String message) throws ExecutionException, InterruptedException, TimeoutException {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);
        future.get(10, TimeUnit.SECONDS);
        System.out.println("Success to send sync message");
    }

    public void routingSend(String topic, String message){
        routingKafkaTemplate.send(topic, message);
    }

    public void byteRoutingSend(String topic, byte[] message){
        routingKafkaTemplate.send(topic, message);
    }

    public void replyingSend(String topic, String message) throws ExecutionException, InterruptedException, TimeoutException {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, message);
        RequestReplyFuture<String, String, String> replyFuture = replyingKafkaTemplate.sendAndReceive(record);

        ConsumerRecord<String, String> consumerRecord = replyFuture.get(10, TimeUnit.SECONDS);
        System.out.println(consumerRecord.value());
    }
}
