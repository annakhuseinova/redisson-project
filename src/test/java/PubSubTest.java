import org.junit.jupiter.api.Test;
import org.redisson.api.RPatternTopicReactive;
import org.redisson.api.RTopicReactive;
import org.redisson.client.codec.StringCodec;

public class PubSubTest extends BaseTest{

    @Test
    public void subscriber1(){
        RTopicReactive topic = this.redissonReactiveClient.getTopic("slack-room", StringCodec.INSTANCE);
        topic.getMessages(String.class)
                .doOnError(System.out::println)
                .doOnNext(System.out::println)
                .subscribe();
        sleep(600000);
    }

    @Test
    public void subscriber2(){
        RTopicReactive topic = this.redissonReactiveClient.getTopic("slack-room", StringCodec.INSTANCE);
        topic.getMessages(String.class)
                .doOnError(System.out::println)
                .doOnNext(System.out::println)
                .subscribe();
        sleep(600000);
    }

    @Test
    public void subscriberByPattern1(){
        RPatternTopicReactive patternTopic = this.redissonReactiveClient.getPatternTopic("slack-room", StringCodec.INSTANCE);
        patternTopic.addListener(String.class, (pattern, channel, msg) -> {
            System.out.println(pattern + " : " + channel + " : " + msg);
        });
        sleep(60000);
    }

    @Test
    public void subscriberByPattern2(){
        RPatternTopicReactive patternTopic = this.redissonReactiveClient.getPatternTopic("slack-room1", StringCodec.INSTANCE);
        patternTopic.addListener(String.class, (pattern, channel, msg) -> {
            System.out.println(pattern + " : " + channel + " : " + msg);
        });
        sleep(60000);
    }
}
