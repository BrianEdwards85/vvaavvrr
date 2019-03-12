package us.edwardstx.vvaavvrr.maps;

import io.vavr.collection.Map;
import org.junit.Test;
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

        System.out.println(src);
    }

    @Test
    public void testVavrImutableMapAppend() {
        Map<Integer, String> src = Map(
                1, "One",
                2, "Two",
                3, "Three",
                4, "Four",
                5, "Five");

        Map<Integer, String> dst = src.put(6,"Six");

        System.out.println(dst);

        assertThat(dst).containsAll(src);
        assertThat(dst).containsAll(Map(6,"Six"));

    }

    @Test
    public void testVavrImutableMapMap(){
        Map<Integer, String> src = Map(
                1, "One",
                2, "Two",
                3, "Three",
                4, "Four",
                5, "Five");

        Map<String, String> dst = src.map((key,value) -> Tuple("[" + key + "]", value));

        System.out.println(dst);
    }


    @Test
    public void testVavrImutableMapMapKeys(){
        Map<Integer, String> src = Map(
                1, "One",
                2, "Two",
                3, "Three",
                4, "Four",
                5, "Five");

        Map<String,String> dst = src.mapKeys((key) -> "[" + key + "]");

        System.out.println(dst);
    }


    @Test
    public void testVavrImutableMapFilter1(){
        Map<Integer, String> src = Map(
                1, "One",
                2, "Two",
                3, "Three",
                4, "Four",
                5, "Five");

        Map<Integer, String> dst = src.filter((key,value) -> key % 2 == 0 );

        System.out.println(dst);
    }

    @Test
    public void testVavrImutableMapFilter2(){
        Map<Integer, String> src = Map(
                1, "One",
                2, "Two",
                3, "Three",
                4, "Four",
                5, "Five");

        Map<Integer, String> dst = src.filter((key,value) -> value.length() == 4 );

        System.out.println(dst);
    }
}
