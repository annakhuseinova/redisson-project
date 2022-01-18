import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBlockingDequeReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class MessageQueueTest extends BaseTest{

    private RBlockingDequeReactive<Long> messageQueue;

    @BeforeAll
    public void setupQueue(){
        messageQueue = this.redissonReactiveClient.getBlockingDeque("message-queue", LongCodec.INSTANCE);
    }

    @Test
    public void consumer1(){
        this.messageQueue.takeElements()
                .doOnNext(element -> System.out.println("Consumer 1: " + element))
                .doOnError(System.out::println)
                .subscribe();
        sleep(600000);
    }

    @Test
    public void consumer2(){
        this.messageQueue.takeElements()
                .doOnNext(element -> System.out.println("Consumer 2: " + element))
                .doOnError(System.out::println)
                .subscribe();
        sleep(600000);
    }

    @Test
    public void producer(){
        Flux.range(1, 100)
                .delayElements(Duration.ofMillis(500))
                .doOnNext(element -> System.out.println("going to add: " + element))
                .flatMap(element -> this.messageQueue.add(Long.valueOf(element)))
                .then();
    }

}
