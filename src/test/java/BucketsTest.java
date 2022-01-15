import org.junit.jupiter.api.Test;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class BucketsTest extends BaseTest{

    /**
     * In order to be able to get multiple key-value pairs, we can use RBuckets / RBucketsReactive object
     * */
    @Test
    public void bucketsAsMap(){
        Mono<Void> mono = this.redissonReactiveClient.getBuckets(StringCodec.INSTANCE)
                .get("user:1:name", "user:2:name", "user:3:name")
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(mono).verifyComplete();
    }
}
