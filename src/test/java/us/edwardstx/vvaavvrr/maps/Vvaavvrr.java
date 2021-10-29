package us.edwardstx.vvaavvrr.maps;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.Function1;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.jackson.datatype.VavrModule;
import org.junit.Test;

import java.io.IOException;

import static io.vavr.API.*;
import static org.assertj.core.api.Assertions.assertThat;

public class Vvaavvrr {
    @Test
    public void testVavrImutableMap() {
        Map<Integer, String> src = Map(
                1, "One",
                2, "Two",
                3, "Three",
                4, "Four",
                5, "Five");

        println(src);
    }

    @Test
    public void testVavrImutableMapAppend() {
        Map<Integer, String> src = Map(
                1, "One",
                2, "Two",
                3, "Three",
                4, "Four",
                5, "Five");

        Map<Integer, String> dst = src.put(6, "Six");

        println(dst);

        assertThat(dst).containsAll(src);
        assertThat(dst).containsAll(Map(6, "Six"));

    }

    @Test
    public void testVavrImutableMapMap() {
        Map<Integer, String> src = Map(
                1, "One",
                2, "Two",
                3, "Three",
                4, "Four",
                5, "Five");

        Map<String, String> dst = src.map((key, value) -> Tuple("[" + key + "]", value.toUpperCase()));

        println(dst);
    }


    @Test
    public void testVavrImutableMapMapKeys() {
        Map<Integer, String> src = Map(
                1, "One",
                2, "Two",
                3, "Three",
                4, "Four",
                5, "Five");

        Map<String, String> dst = src.mapKeys((key) -> "[" + key + "]");
        println(dst);
    }


    @Test
    public void testVavrImutableMapFilter1() {
        Map<Integer, String> src = Map(
                1, "One",
                2, "Two",
                3, "Three",
                4, "Four",
                5, "Five");

        Map<Integer, String> dst = src.filter((key, value) -> key % 2 == 0);

        println(dst);
    }

    @Test
    public void testVavrImutableMapFilter2() {
        Map<Integer, String> src = Map(
                1, "One",
                2, "Two",
                3, "Three",
                4, "Four",
                5, "Five");

        Map<Integer, String> dst = src.filter((key, value) -> value.length() == 4);

        println(dst);
    }

    @Test
    public void testInterop() {
        Map<Integer, String> src = Map(
                1, "One",
                2, "Two",
                3, "Three",
                4, "Four",
                5, "Five");

        java.util.Map<Integer, String> javaMap = src.toJavaMap();

        assertThat(javaMap).containsOnlyKeys(1, 2, 3, 4, 5);
    }

    @Test
    public void testJSON() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new VavrModule());
        TypeReference<Map<String,Integer>> typeReference = new TypeReference< Map<String,Integer> >() { };

        String json = "{ \"one\": 1, \"two\": 2, \"three\": 3 }";
        Map<String,Integer> map = mapper.readValue(json, typeReference);

        println(map);

        assertThat(map).containsExactly(Tuple("one", 1), Tuple("two", 2), Tuple("three", 3));
    }

    @Test
    public void testZip() {
        List<Integer> numbers = List(1, 2, 3);
        List<String> strings = List("one", "two", "three");

        List<Tuple2<Integer, String>> zip = numbers.zip(strings);

        Map<Integer, String> map = zip.toMap(Function1.identity());

        println(map);
    }
}