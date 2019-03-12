package us.edwardstx.vvaavvrr.lists;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

public class Past {
    @Test
    public void testJava7Collections(){
        List<String> src = new LinkedList<>();
        src.add("one");
        src.add("two");
        src.add("three");

        List<String> dst = new LinkedList<>();
        for(String s : src){
            dst.add(s.toUpperCase());
        }

        System.out.println(dst);

        assertThat(src).containsExactly("one", "two", "three");
        assertThat(dst).containsExactly("ONE", "TWO", "THREE");
    }

    @Test
    public void testJava8Collections(){
        List<String> src = new LinkedList<>();
        src.add("one");
        src.add("two");
        src.add("three");

        List<String> dst = src
                .stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        System.out.println(dst);

        assertThat(src).containsExactly("one", "two", "three");
        assertThat(dst).containsExactly("ONE", "TWO", "THREE");
    }

    @Test
    public void testJava8GuavaCollections(){
        List<String> src = ImmutableList.of("one", "two", "three");

        List<String> dst = src
                .stream()
                .map(String::toUpperCase)
                .collect(ImmutableList.toImmutableList());

        System.out.println(dst);

        assertThat(src).containsExactly("one", "two", "three");
        assertThat(dst).containsExactly("ONE", "TWO", "THREE");
    }


    @Test
    public void testJava8MutableCollections(){
        List<String> src = new LinkedList<>();
        src.add("one");
        src.add("two");
        src.add("three");

        List<String> dst = appendDefault1(src);

        System.out.println(dst);
        assertThat(dst).containsExactly("one", "two", "three", "zero");

        System.out.println(src);
        assertThat(src).containsExactly("one", "two", "three");
    }

    public List<String> appendDefault1(List<String> in){
        in.add("zero");
        return in;
    }

    @Test
    public void testJava8GuavaImutableCollections(){
        List<String> src = ImmutableList.of("one", "two", "three");

        List<String> dst = appendDefault2(src);

        System.out.println(dst);
        assertThat(dst).containsExactly("one", "two", "three", "zero");

        System.out.println(src);
        assertThat(src).containsExactly("one", "two", "three");
    }

    public List<String> appendDefault2(List<String> in){
        return ImmutableList.<String>builder()
                .addAll(in)
                .add("zero")
                .build();
    }


    @Test
    public void testJava8Reduce(){
        List<String> src = ImmutableList.of("one", "two", "three");

        String reduced = src
                .stream()
                .reduce((l,r) -> l + ", " + r)
                .get();

        System.out.println(reduced);

        assertThat(reduced).isEqualTo("one, two, three");
    }

    @Test
    public void testJavaFilter(){
        List<String> src = ImmutableList.of("one", "two", "three");

        List<String> filtered = src
                .stream()
                .filter(s -> s.length() <= 3)
                .collect(ImmutableList.toImmutableList());

        System.out.println(filtered);

        assertThat(filtered).containsExactly("one", "two");
    }

}
