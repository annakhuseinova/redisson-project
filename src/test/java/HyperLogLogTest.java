import org.junit.jupiter.api.Test;
import org.redisson.api.RHyperLogLogReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class HyperLogLogTest extends BaseTest{

    @Test
    public void count(){
        RHyperLogLogReactive<Long> hyperLogLogCounter = this.redissonReactiveClient.getHyperLogLog("user:visits", LongCodec.INSTANCE);
        List<Long> longList1 = LongStream.rangeClosed(1, 25000)
                .boxed()
                .collect(Collectors.toList());
        List<Long> list2 = LongStream.range(25001, 50000).boxed().collect(Collectors.toList());
        List<Long> list3 = LongStream.range(1, 75000).boxed().collect(Collectors.toList());
        List<Long> list4 = LongStream.range(50000, 100000).boxed().collect(Collectors.toList());

        Flux.just(longList1, list2, list3, list4).flatMap(hyperLogLogCounter::addAll)
                        .then();
        StepVerifier.create(hyperLogLogCounter.addAll(longList1).then()).verifyComplete();
        hyperLogLogCounter.count().doOnNext(System.out::println).subscribe();



    }
}
