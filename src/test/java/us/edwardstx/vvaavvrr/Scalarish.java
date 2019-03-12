package us.edwardstx.vvaavvrr;

import com.google.common.base.Function;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.Test;

import java.util.Map;

import static io.vavr.API.*;
import static org.assertj.core.api.Assertions.assertThat;

public class Scalarish {

    @Test
    public void testBasicTuple(){
        Tuple2<Integer,String> one = Tuple(1, "One");
        String applied = one.apply((l, r) -> "[" + l + "] " + r);
        System.out.println(applied);

        assertThat(applied).isEqualTo("[1] One");

        Integer i = one._1;

        assertThat(i).isEqualTo(1);

        String s = one._2;

        assertThat(s).isEqualTo("One");

        Map.Entry<Integer,String> integerStringEntry = one.toEntry();

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
        System.out.println("optionValue: " + in);
        if(in % 4 == 0){
            return Option(in);
        }
        return None();
    }
    
    @Test
    public void testVavrOption(){
        Option<Long> o1 = optionValue(1L)
                .orElse(() -> optionValue(2L));

        System.out.println("O1: " + o1.getOrElse(0L));

        Option<Long> o2 = o1.orElse(() -> optionValue(3L))
                .orElse(() -> optionValue(4L))
                .orElse(() -> optionValue(5L));

        System.out.println("O2: " + o2.getOrElse(0L));
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

        System.out.println(map.mkString(", "));

        List<Long> filterMap = map.filter(Option::isDefined).map(Option::get);

        System.out.println(filterMap.mkString(", "));

        List<Long> flatMap = map.flatMap(Function1.identity());

        System.out.println(flatMap.mkString(", "));
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
        Try<Long> l1 = tryDouble(3L);
        String tried = l1
                .map((l) -> "[" + l + "]")
                .getOrElse("none");
        System.out.println("T2: " + tried);
    }
}
