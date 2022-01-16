import org.junit.jupiter.api.Test;
import org.redisson.api.RListReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ListTest extends BaseTest{

    @Test
    public void listTest(){
        RListReactive<Long> list = this.redissonReactiveClient.getList("number-input", LongCodec.INSTANCE);
        Mono<Void> listAdd = Flux.range(1, 10)
                .map(Long::valueOf)
                .flatMap(list::add)
                .then();
        StepVerifier.create(listAdd)
                .verifyComplete();
        StepVerifier.create(list.size())
                .expectNext(10)
                .verifyComplete();
    }
}
