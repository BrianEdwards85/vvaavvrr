package us.edwardstx.vvaavvrr.lists;

import com.google.common.collect.ImmutableList;
import io.vavr.collection.List;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Vvaavvrr {
    @Test
    public void testVavrImutableCollections(){
        List<String> src = List.of("one", "two", "three");

        List<String> dst = src.map(String::toUpperCase);

        System.out.println(dst);

        assertThat(src).containsExactly("one", "two", "three");
        assertThat(dst).containsExactly("ONE", "TWO", "THREE");
    }

    @Test
    public void testVavrImutableCollectionsAppend(){
        List<String> src = List.of("one", "two", "three");

        List<String> dst = appendDefault1(src);

        System.out.println(dst);
        assertThat(dst).containsExactly("one", "two", "three", "zero");

        System.out.println(src);
        assertThat(src).containsExactly("one", "two", "three");
    }

    public List<String> appendDefault1(List<String> in){
        return in.append("zero");
    }


    @Test
    public void testVavrReduce(){
        List<String> src = List.of("one", "two", "three");

        String reduced = src.reduce((l,r) -> l + ", " + r);
        System.out.println(reduced);

        assertThat(reduced).isEqualTo("one, two, three");

        String mkString = src.mkString(", ");
        assertThat(reduced).isEqualTo(mkString);

    }

    @Test
    public void testVavrFilter(){
        List<String> src = List.of("one", "two", "three");

        List<String> filtered = src.filter(s -> s.length() <= 3);

        System.out.println(filtered);
        assertThat(filtered).containsExactly("one", "two");
    }

    @Test
    public void testInterop(){
        List<String> src = List.of("one", "two", "three");
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
}
