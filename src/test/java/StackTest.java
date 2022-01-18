import org.junit.jupiter.api.Test;
import org.redisson.api.RDequeReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class StackTest extends BaseTest{

    @Test
    public void stackTest(){
        RDequeReactive<Object> deque = this.redissonReactiveClient.getDeque("number-input", LongCodec.INSTANCE);
        Mono<Void> stackPoll = deque.pollLast()
                .repeat(3)
                .doOnNext(System.out::println)
                .then();
        StepVerifier.create(stackPoll)
                .verifyComplete();
        StepVerifier.create(deque.size())
                .expectNext(2)
                .verifyComplete();
    }
}
