import org.junit.jupiter.api.Test;
import org.redisson.api.DeletedObjectListener;
import org.redisson.api.ExpiredObjectListener;
import org.redisson.api.ObjectListener;
import org.redisson.api.RBucketReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.TimeUnit;

public class EventListenerTest extends BaseTest{

    /**
     * We can get notified when a key expires in Redis. BUT! This functionality is by default disabled in Redis
     * because feature uses some CPU power.
     *
     * Notifications are enabled using the notify-keyspace-events
     * */
    @Test
    public void expiredEventTest(){
        RBucketReactive<String> bucket = this.redissonReactiveClient.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("sam", 10, TimeUnit.SECONDS);
        Mono<Void> get = bucket.get()
                .doOnNext(System.out::println)
                .then();

        Mono<Void> event = bucket.addListener(new ExpiredObjectListener() {
            @Override
            public void onExpired(String s) {
                System.out.println("Expired: " + s);
            }
        }).then();
        StepVerifier.create(set.concatWith(get)).verifyComplete();

        sleep(11000);
    }

    @Test
    public void deleteEventTest(){
        RBucketReactive<String> bucket = this.redissonReactiveClient.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("sam");
        Mono<Void> get = bucket.get().doOnNext(System.out::println).then();
        Mono<Void> event = bucket.addListener(new DeletedObjectListener() {
            @Override
            public void onDeleted(String s) {
                System.out.println("Deleted: " + s);
            }
        }).then();

        StepVerifier.create(set.concatWith(get).concatWith(event)).verifyComplete();
        sleep(11000);
    }
}
