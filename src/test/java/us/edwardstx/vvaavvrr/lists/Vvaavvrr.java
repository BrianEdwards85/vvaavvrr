package us.edwardstx.vvaavvrr.lists;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.jackson.datatype.VavrModule;
import org.junit.Test;

import java.io.IOException;

import static io.vavr.API.*;
import static org.assertj.core.api.Assertions.assertThat;

public class Vvaavvrr {
    @Test
    public void testVavrImutableCollections(){
        List<String> src = List("one", "two", "three");

        List<String> dst = src.map(String::toUpperCase);

        println(dst);

        assertThat(src).containsExactly("one", "two", "three");
        assertThat(dst).containsExactly("ONE", "TWO", "THREE");
    }

    @Test
    public void testVavrImutableCollectionsAppend(){
        List<String> src = List("one", "two", "three");

        List<String> dst = appendDefault1(src);

        println(dst);
        assertThat(dst).containsExactly("one", "two", "three", "zero");

        println(src);
        assertThat(src).containsExactly("one", "two", "three");
    }

    public List<String> appendDefault1(List<String> in){
        return in.append("zero");
    }


    @Test
    public void testVavrReduce(){
        List<String> src = List("one", "two", "three");

        String reduced = src.reduce((l,r) -> l + ", " + r);
        println(reduced);

        assertThat(reduced).isEqualTo("one, two, three");

        String reduced2 = src.mkString(", ");
        assertThat(reduced).isEqualTo(reduced2);
    }

    @Test
    public void testVavrReduce2() {
        List<Integer> src = List(1, 2, 3, 4, 5);

        assertThat(src.sum()).isEqualTo(15L);
        assertThat(src.average().get()).isEqualTo(3.0);
    }

    @Test
    public void testVavrSort(){
        List<Integer> src = List(3, 1, 5, 4, 2);

        List<Integer> sorted = src.sorted();

        println(src);
        println(sorted);

        assertThat(sorted).isEqualTo(List(1, 2, 3, 4, 5));
    }

    @Test
    public void testVavrFilter(){
        List<String> src = List("one", "two", "three");

        List<String> filtered = src.filter(s -> s.length() <= 3);

        println(filtered);
        assertThat(filtered).containsExactly("one", "two");
    }

    @Test
    public void testInterop(){
        List<String> src = List("one", "two", "three");
        java.util.List<String> java = ImmutableList.of("one", "two", "three");

        java.util.List<String> view = src.asJava();

        assertThat(java).isEqualTo(view);
    }


    @Test
    public void testInterop2(){
        java.util.List<String> java = ImmutableList.of("one", "two", "three");

        List<String> vavr = List.ofAll(java);

        java.util.List<String> view = vavr.asJava();

        assertThat(java).isEqualTo(view);
    }

    @Test
    public void testInterop3() {
        List<String> src = List("one", "two", "three");

        Iterable<String> iterable = src;

        for(String e : src){
            println(e);
        }
    }

    @Test
    public void testSlice(){
        List<String> src = List("one", "two", "three", "four", "five");
        List<String> slice = src.slice(2, 4);

        println(slice);

        assertThat(slice).containsExactly("three", "four");
    }

    @Test
    public void testGroupBy(){
        List<String> src = List("one", "two", "three", "four", "five");
        final Map<Integer, List<String>> groups = src.groupBy(String::length);

        println(groups);
    }

    @Test
    public void testJSON() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new VavrModule());
        TypeReference<List<Integer>> typeReference = new TypeReference<List<Integer>>() {};

        String json = "[1, 2, 3, 4]";

        List<Integer> list = mapper.readValue(json, typeReference);

        println(list);

        assertThat(list).containsExactly(1,2,3,4);
    }
}
