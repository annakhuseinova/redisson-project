import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.client.codec.LongCodec;

public class TransactionTest extends BaseTest{

    @Test
    public void nonTransactionTest(){
        RBucketReactive<Long> user1Balance = this.redissonReactiveClient.getBucket("user:1:balance", LongCodec.INSTANCE);
        RBucketReactive<Long> user2Balance = this.redissonReactiveClient.getBucket("user:2:balance", LongCodec.INSTANCE);

    }
}
