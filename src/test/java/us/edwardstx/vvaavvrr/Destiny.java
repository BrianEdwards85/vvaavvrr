package us.edwardstx.vvaavvrr;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.concurrent.Future;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.Random;
import java.util.UUID;

import static io.vavr.API.*;
import static org.assertj.core.api.Assertions.assertThat;

public class Destiny {

    public static Random random = new Random();

    public static Long delay() {
        return (random.nextInt(4) + 1) * 400 + random.nextInt(600) + 1L;
    }

    public static String hardWork() throws InterruptedException {
        Thread.sleep(delay());
        return UUID.randomUUID().toString();
    }
    public static Future<String> hardWorkFuture() {
        return Future(Destiny::hardWork);
    }

    @Test
    public void simpleFuture() {
        Future<String> f1 = hardWorkFuture();
        println("Returned");
        assertThat(f1.isCompleted()).isFalse();
        f1.await();
        assertThat(f1.isCompleted()).isTrue();
        assertThat(f1.get()).isNotBlank();
        println(f1.get());
    }

    @Test
    public void mapFuture() {
        Future<String> f1 = hardWorkFuture().map(s -> s.substring(0, 6));
        println("Returned");
        assertThat(f1.isCompleted()).isFalse();
        f1.await();
        assertThat(f1.isCompleted()).isTrue();
        assertThat(f1.get()).isNotBlank();
        assertThat(f1.get()).doesNotContain("-");
        println(f1.get());
    }

    public Future<String> capitalize(String string) {
        return Future(() -> {
            Thread.sleep(delay());
            if (StringUtils.isAllBlank(string)) {
                throw new IllegalArgumentException("Blank string not allowed");
            }
            return string.toUpperCase();
        });
    }

    @Test
    public void flatMapFuture() {
        Future<String> f1 = hardWorkFuture()
                .map(s -> s.substring(0, 6))
                .flatMap(this::capitalize);
        println("Returned");
        assertThat(f1.isCompleted()).isFalse();
        f1.await();
        assertThat(f1.isCompleted()).isTrue();
        assertThat(f1.get()).isNotBlank();
        assertThat(f1.get()).doesNotContain("-");
        assertThat(f1.get()).doesNotContain("abcdef");
        println(f1.get());
    }

    @Test
    public void sequenceFuture() {
        List<String> list = List("ab", "bc", "cd", "de");
        List<Future<String>> listOfFututres = list.map(this::capitalize);
        listOfFututres.forEach(f -> assertThat(!f.isCompleted()));
        Future<Seq<String>> futureOfSeq = Future.sequence(listOfFututres);
        final Future<List<String>> futureOfList = futureOfSeq.map(List::ofAll);
        futureOfList.await();
        listOfFututres.forEach(f -> assertThat(f.isCompleted()));
        Seq<String> strings = futureOfList.get();
        assertThat(strings).contains("AB", "BC", "CD", "DE");
        println(strings.mkString(", "));
    }

    @Test
    public void failureIsAlwaysAnOption() {
        Future<String> theFutureIsFail = capitalize("");
        println("Returned");
        assertThat(theFutureIsFail.isCompleted()).isFalse();
        theFutureIsFail.await();
        assertThat(theFutureIsFail.isFailure());
    }

    @Test
    public void butYouShouldAlwaysRecover(){
        Future<String> theFutureIsFail = capitalize(""); //below
        println("Returned");
        assertThat(theFutureIsFail.isCompleted()).isFalse();

        Future<String> mappedFuture = theFutureIsFail.map(s -> "[" + s + "]");
        Future<String> orElse = mappedFuture.orElse(Future("I failed :("));
        orElse.await();

        String r = orElse.get();
        println(r);
        assertThat(r).isEqualTo("I failed :(");
    }

    @Test
    public void successIsBetter(){
        Future<String> theFutureIsNotFail = capitalize("abcde");
        println("Returned");
        assertThat(theFutureIsNotFail.isCompleted()).isFalse();

        Future<String> mappedFuture = theFutureIsNotFail.map(s -> "[" + s + "]");

        Future<String> orElse = mappedFuture.orElse(Future("I failed :("));
        orElse.await();

        String r = orElse.get();
        assertThat(r).isEqualTo("[ABCDE]");
        println(r);
    }

    @Test
    public void explainYourself(){
        Future<String> theFutureIsFail = capitalize("");
        println("Returned");
        assertThat(theFutureIsFail.isCompleted()).isFalse();

        Future<String> recover = theFutureIsFail.recover(t -> {
            return "I failed: " + t.getMessage();
        });

        recover.await();

        String result = recover.get();
        assertThat(result).isEqualTo("I failed: Blank string not allowed");
        println(result);
    }
}
