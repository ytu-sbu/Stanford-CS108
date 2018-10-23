// TabooTest.java
// Taboo class tests -- nothing provided.

import java.util.*;

import junit.framework.TestCase;

public class TabooTest extends TestCase {

    public void testNoFollow1() {
        List<Character> rules = new ArrayList<>();
        String s = "acabdcd";
        for (int i = 0; i < s.length(); i++) {
            rules.add(s.charAt(i));
        }
        Taboo<Character> taboo = new Taboo<>(rules);
        Set<Character> a = new HashSet<>(Arrays.asList('c', 'b'));
        assertTrue(a.equals(taboo.noFollow('a')));
        Set<Character> c = new HashSet<>(Arrays.asList('a', 'd'));
        assertTrue(c.equals(taboo.noFollow('c')));
        Set<Character> b = new HashSet<>(Arrays.asList('d'));
        assertTrue(b.equals(taboo.noFollow('b')));
        Set<Character> d = new HashSet<>(Arrays.asList('c'));
        assertTrue(d.equals(taboo.noFollow('d')));
        assertTrue(Collections.EMPTY_SET.equals(taboo.noFollow('z')));
    }

    public void testNoFollow2() {
        List<Character> rules = new ArrayList<>();
        rules.add('a');
        rules.add('b');
        rules.add(null);
        rules.add('c');
        rules.add('d');
        Taboo<Character> taboo = new Taboo<>(rules);
        assertTrue(Collections.EMPTY_SET.equals(taboo.noFollow('b')));
    }

    public void testReduce() {
        List<Character> rules = new ArrayList<>();
        String s = "acab";
        for (int i = 0; i < s.length(); i++) {
            rules.add(s.charAt(i));
        }
        Taboo<Character> taboo = new Taboo<>(rules);

        List<Character> word = new ArrayList<>();
        String w = "acbxca";
        for (int i = 0; i < w.length(); i++) {
            word.add(w.charAt(i));
        }
        taboo.reduce(word);
        List<Character> reduced = Arrays.asList('a', 'x', 'c');
        assertTrue(reduced.equals(word));
    }
}
