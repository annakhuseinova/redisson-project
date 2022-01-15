import dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMapCacheReactive;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MapCacheTest extends BaseTest{

    @Test
    public void mapCacheTest(){
        TypedJsonJacksonCodec codec = new TypedJsonJacksonCodec(Integer.class, Student.class);
        RMapCacheReactive<Integer, Student> mapCache = this.redissonReactiveClient.getMapCache("users:cache", codec);
        Student student1 = new Student("sam", 30, "atlanta", List.of(1,2,3 ));
        Student student2 = new Student("jake", 30, "miami", List.of(1,2,3));

        Mono<Student> studentMono1 = mapCache.put(1, student1, 5, TimeUnit.SECONDS);
        Mono<Student> studentMono2 = mapCache.put(2, student2, 5, TimeUnit.SECONDS);

        StepVerifier.create(studentMono1.concatWith(studentMono2).then())
                .verifyComplete();

        sleep(3000);

        mapCache.get(1).doOnNext(System.out::println).subscribe();
        mapCache.get(2).doOnNext(System.out::println).subscribe();

        sleep(3000);

        mapCache.get(1).doOnNext(System.out::println).subscribe();
        mapCache.get(2).doOnNext(System.out::println).subscribe();
    }
}
