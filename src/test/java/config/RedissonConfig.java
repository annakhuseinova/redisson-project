package config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.config.Config;

import java.util.Objects;

public class RedissonConfig {

    private RedissonClient redissonClient;

    /**
     * An alternative to Config class is configuration via yaml
     * */
    public RedissonClient getClient(){
        if (Objects.isNull(redissonClient)){
            Config config = new Config();
            config.useSingleServer()
                    .setAddress("redis://127.0.0.1:6379")
                    .setUsername("...")
                    .setPassword("...");
            redissonClient = Redisson.create(config);
        }
        return redissonClient;
    }

    /**
     * Конфигурация реактивного клиента
     * */
    public RedissonReactiveClient getReactiveClient(){
        return getClient().reactive();
    }

}
