import dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMapReactive;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

public class MapTest extends BaseTest{

    @Test
    public void mapTest(){
        RMapReactive<String, String> map = this.redissonReactiveClient.getMap("user:1", StringCodec.INSTANCE);
        Mono<String> name = map.put("name", "sam");
        Mono<String> age = map.put("age", "10");
        Mono<String> city = map.put("city", "atlanta");
        StepVerifier.create(name.concatWith(age).concatWith(city).then()).verifyComplete();
    }

    @Test
    public void anotherMapTest(){
        RMapReactive<String, String> map = this.redissonReactiveClient.getMap("user:2", StringCodec.INSTANCE);
        Map<String, String> java = Map.of(
            "name", "jake",
            "age", "30",
            "city", "miami"
        );
        StepVerifier.create(map.putAll(java).then())
                .verifyComplete();

    }

    @Test
    public void mapCustomObjectTest(){
        TypedJsonJacksonCodec codec = new TypedJsonJacksonCodec(Integer.class, Student.class);
        RMapReactive<Integer, Student> map = this.redissonReactiveClient.getMap("users", codec);
        Student student1 = new Student("sam", 30, "atlanta", List.of(1,2,3 ));
        Student student2 = new Student("jake", 30, "miami", List.of(1,2,3));

        Mono<Student> mono1 = map.put(1, student1);
        Mono<Student> mono2 = map.put(2, student2);

        StepVerifier.create(mono1.concatWith(mono2).then()).verifyComplete();
    }
}
