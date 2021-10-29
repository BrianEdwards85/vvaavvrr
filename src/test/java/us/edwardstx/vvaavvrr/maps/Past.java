package us.edwardstx.vvaavvrr.maps;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;


public class Past {

    @Test
    public void testJava7Collections(){
        Map<Integer, String> src = new HashMap<>();
        src.put(1, "One");
        src.put(2, "Two");
        src.put(3, "Three");
        src.put(4, "Four");
        src.put(5, "Five");

        System.out.println(src);
    }


    @Test
    public void testJava8CollectionsAppend(){
        Map<Integer, String> src1 = new HashMap<>();
        src1.put(1, "One");
        src1.put(2, "Two");
        src1.put(3, "Three");
        src1.put(4, "Four");
        src1.put(5, "Five");

        Map<Integer, String> src2 = new HashMap<>();
        src2.put(6, "Six");

        Map<Integer, String> dst = Stream
                .concat(src1.entrySet().stream(), src2.entrySet().stream())
                .collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));

        System.out.println(dst);

        assertThat(dst).containsAllEntriesOf(src1);

        assertThat(dst).containsAllEntriesOf(src2);
    }

    @Test
    public void testJava8GuavaCollections(){
        Map<Integer, String> src = ImmutableMap.of(
                1, "One",
                2, "Two",
                3, "Three",
                4, "Four",
                5, "Five");

        System.out.println(src);
    }

    @Test
    public void testJava8GuavaCollectionsAppend(){
        Map<Integer, String> src = ImmutableMap.of(
                1, "One",
                2, "Two",
                3, "Three",
                4, "Four",
                5, "Five");

        Map<Integer, String> dst = ImmutableMap.<Integer, String> builder()
                .putAll(src)
                .put(6, "Six")
                .build();

        System.out.println(dst);

        assertThat(dst).containsAllEntriesOf(src);

        assertThat(dst).contains(new AbstractMap.SimpleEntry<>(6, "Six"));
    }

    @Test
    public void testJava8Map(){
        Map<Integer, String> src = ImmutableMap.of(
                1, "One",
                2, "Two",
                3, "Three",
                4, "Four",
                5, "Five");

        Map<Object, Object> dst = src
                .entrySet()
                .stream()
                .map((e) -> new AbstractMap.SimpleEntry("[" + e.getKey() + "]", e.getValue()))
                .collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));

        System.out.println(dst);
    }


    @Test
    public void testJava8Filter1(){
        Map<Integer, String> src = ImmutableMap.of(
                1, "One",
                2, "Two",
                3, "Three",
                4, "Four",
                5, "Five");

        Map<Object, Object> dst = src
                .entrySet()
                .stream()
                .filter((e) -> e.getKey() % 2 == 0)
                .collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));

        System.out.println(dst);
    }
    @Test
    public void testJava8Filter2(){
        Map<Integer, String> src = ImmutableMap.of(
                1, "One",
                2, "Two",
                3, "Three",
                4, "Four",
                5, "Five");

        Map<Object, Object> dst = src
                .entrySet()
                .stream()
                .filter((e) -> e.getValue().length() == 4)
                .collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));

        System.out.println(dst);
    }

}
