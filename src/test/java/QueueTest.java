import org.junit.jupiter.api.Test;
import org.redisson.api.RQueueReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class QueueTest extends BaseTest{

    @Test
    public void setupQueue(){
        RQueueReactive<Long> queue = this.redissonReactiveClient.getQueue("number-input", LongCodec.INSTANCE);
        Mono<Void> queueMono = queue.poll()
                .repeat(3)
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(queueMono)
                        .verifyComplete();
        StepVerifier.create(queue.size())
                .expectNext(6)
                .verifyComplete();
    }
}
