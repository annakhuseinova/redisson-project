import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.RScoredSortedSetReactive;
import org.redisson.codec.TypedJsonJacksonCodec;
import priorityqueue.Category;
import priorityqueue.PriorityQueue;
import priorityqueue.UserOrder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

public class PriorityQueueTest extends BaseTest {

    private PriorityQueue priorityQueue;

    @BeforeAll
    public void setupQueue(){
        RScoredSortedSetReactive<UserOrder> scoredSortedSet = this.redissonReactiveClient.getScoredSortedSet("user:order:queue",
                new TypedJsonJacksonCodec(UserOrder.class));
        this.priorityQueue = new PriorityQueue(scoredSortedSet);
    }

    @Test
    public void producer(){
        UserOrder userOrder1 = new UserOrder(1, Category.GUEST);
        UserOrder userOrder2 = new UserOrder(2, Category.STANDARD);
        UserOrder userOrder3 = new UserOrder(3, Category.PRIME);
        UserOrder userOrder4 = new UserOrder(4, Category.STANDARD);
        UserOrder userOrder5 = new UserOrder(5, Category.GUEST);
        Mono<Void> mono = Flux.just(userOrder1, userOrder2, userOrder3, userOrder4, userOrder5)
                .flatMap(this.priorityQueue::add)
                .then();
        StepVerifier.create(mono).verifyComplete();
    }

    @Test
    public void consumer(){
        this.priorityQueue.takeItems()
                .delayElements(Duration.ofMillis(500))
                .doOnNext(System.out::println)
                .subscribe();
        sleep(600000);
    }
}
