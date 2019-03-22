package us.edwardstx.vvaavvrr;

import com.google.common.base.Function;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.Test;

import static io.vavr.API.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.vavr.api.VavrAssertions.assertThat;

import java.util.Map.Entry;

public class Scalarish {

    @Test
    public void testBasicTuple() {
        Tuple2<Integer, String> one = Tuple(1, "One");
        String applied = one.apply((l, r) -> "[" + l + "] " + r);
        println(applied);

        assertThat(applied).isEqualTo("[1] One");

        Integer i = one._1;

        assertThat(i).isEqualTo(1);

        String s = one._2;

        assertThat(s).isEqualTo("One");

        Entry<Integer, String> integerStringEntry = one.toEntry();

        Tuple3<Integer,String,Boolean> append = one.append(true);
    }

    @Test
    public void testPartialFunctionJava8(){

        Function<Integer, Integer> zeroOrGreater = (x) -> Math.max(0, x);

        assertThat(zeroOrGreater.apply(4)).isEqualTo(4);

        assertThat(zeroOrGreater.apply(-2)).isEqualTo(0);
    }

    @Test
    public void testPartialFunctionVavr(){
        Function2<Integer, Integer, Integer> max = Math::max;

        Function1<Integer,Integer> zeroOrGreater = max.apply(0);

        assertThat(zeroOrGreater.apply(4)).isEqualTo(4);

        assertThat(zeroOrGreater.apply(-2)).isEqualTo(0);
    }

    public static Option<Long> optionValue(Long in){
        println("optionValue: " + in);
        if(in % 4 == 0){
            return Option(in);
        }
        return None();
    }

    @Test
    public void testVavrOption(){
        Option<Long> o1 = optionValue(1L)
                .orElse(() -> optionValue(2L));

        println("O1: " + o1.getOrElse(0L));

        Option<Long> o2 = o1.orElse(() -> optionValue(3L))
                .orElse(() -> optionValue(4L))
                .orElse(() -> optionValue(5L));

        println("O2: " + o2.getOrElse(0L));
    }

    @Test
    public void testVavrOptionToList(){
        Option<Long> none = None();

        List<Long> emptyList = none.toList();
        assertThat(emptyList).isNotNull();
        assertThat(emptyList).isEmpty();

        Option<Long> some = Some(3L);
        List<Long> someList = some.toList();
        assertThat(someList).contains(3L);
    }

    @Test
    public void testVavrAppendOptionalTOList(){
        Option<Long> none = None();
        List<Object> list = List(1L);

        List<Object> addedNone = list.appendAll(none);
        assertThat(addedNone).containsExactly(1L);

        Option<Long> some = Some(3L);
        List<Object> added3 = list.appendAll(some);
        assertThat(added3).containsExactly(1L, 3L);

    }

    @Test
    public void testVavrStream(){
        List<Long> numbers = Stream
                .iterate(3L, i -> i + 3)
                .take(25).toList();

        List<Option<Long>> map = numbers.map(n -> {
            if (n % 2 == 0)
                return Option(n);
            else
                return Option.none();
        });

        println(map.mkString(", "));

        List<Long> filterMap = map.filter(Option::isDefined).map(Option::get);

        println(filterMap.mkString(", "));

        List<Long> flatMap = map.flatMap(Function1.identity());

        println(flatMap.mkString(", "));
    }

    @Test
    public void inifinitMap(){
        Map<Integer, String> map = Map(Tuple(1, "one"), Tuple(2, "two"), Tuple(3, "three"),
                Tuple(4, "four"), Tuple(5, "five"), Tuple(6, "six"), Tuple(7, "seven"), Tuple(8, "eight"),
                Tuple(9, "nine"), Tuple(10, "ten"), Tuple(11, "eleven"), Tuple(12, "tweleve"), Tuple(13, "thirteen"));
        assertThat(map.keySet()).containsAll(Stream.from(1).take(12));
        println(map);
    }

    public Long buggyDouble(Long in){
        if(in % 3 == 0)
            throw new RuntimeException("So bad :{");
        return in * 2;
    }

    public Try<Long> tryDouble(Long in){
        return Try(() -> buggyDouble(in));
    }

    @Test
    public void testVavrTry(){
        Try<Long> l3 = tryDouble(3L);
        String tried3 = l3
                .map(l -> "[" + l + "]")
                .getOrElse("none");
        println("T3: " + tried3);

        assertThat(tried3).isEqualTo("none");
        assertThat(l3).failReasonHasMessage("So bad :{");

        Try<Long> l4 = tryDouble(4L);
        String tried4 = l4
                .map(l -> "[" + l + "]")
                .getOrElse("none");
        println("T4: " + tried4);

        assertThat(tried4).isEqualTo("[8]");
        assertThat(l4).contains(8L);
    }

    public Try<Long> tryTriple(Long in){
        if(in % 2 == 0) {
            return Failure(new RuntimeException("So bad :{"));
        }
        return Success(in * 3);
    }
}
