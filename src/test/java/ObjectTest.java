import dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

public class ObjectTest extends BaseTest{

    /**
     * JsonJacksonCodec also stores the class name in json object, to get rid of that we can switch to
     * TypedJsonJacksonCodec where we specify the class
     * */
    @Test
    public void objectTest(){
        Student student = new Student("marshal", 10, "atlanta", List.of(1,2,3));
        RBucketReactive<Student> bucket = this.redissonReactiveClient.getBucket("student:1", new TypedJsonJacksonCodec(Student.class));
        Mono<Void> set = bucket.set(student);
        Mono<Void> get = bucket.get().doOnNext(System.out::println).then();
        StepVerifier.create(set.concatWith(get)).verifyComplete();
    }
}
