import org.junit.jupiter.api.Test;
import org.redisson.api.BatchOptions;
import org.redisson.api.RBatchReactive;
import org.redisson.api.RListReactive;
import org.redisson.api.RSetReactive;
import org.redisson.client.codec.LongCodec;
import reactor.test.StepVerifier;

public class BatchTest extends BaseTest{

    /**
     * We can create overridden BatchOptions. Here we use the default ones
     * */
    @Test
    public void batchTest(){
        RBatchReactive batch = this.redissonReactiveClient.createBatch(BatchOptions.defaults());
        RListReactive<Long> list = batch.getList("numbers-list", LongCodec.INSTANCE);
        RSetReactive<Long> set = batch.getSet("numbers-set", LongCodec.INSTANCE);
        for (long i = 0; i < 20000; i++) {
            list.add(i);
            set.add(i);
        }

        StepVerifier.create(batch.execute().then())
                .verifyComplete();
    }
}
