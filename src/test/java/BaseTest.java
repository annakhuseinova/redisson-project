import config.RedissonConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.redisson.api.RedissonReactiveClient;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {

    private final RedissonConfig redissonConfig = new RedissonConfig();
    private RedissonReactiveClient redissonReactiveClient;

    @BeforeAll
    public void setClient(){
        this.redissonReactiveClient = redissonConfig.getReactiveClient();
    }

    @AfterAll
    public void shutdown(){
        this.redissonReactiveClient.shutdown();
    }

}
