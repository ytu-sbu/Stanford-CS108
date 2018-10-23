import java.util.*;

public class Appearances {
	
	/**
	 * Returns the number of elements that appear the same number
	 * of times in both collections. Static method. (see handout).
	 * @return number of same-appearance elements
	 */
	public static <T> int sameCount(Collection<T> a, Collection<T> b) {
        Map<T, Integer> countA = new HashMap<>();
        Map<T, Integer> countB = new HashMap<>();
        count(countA, a);
        count(countB, b);

        if (a.size() > b.size()) {
            return getSameCount(countA, countB, b);
        }
        else {
            return getSameCount(countA, countB, a);
        }
	}

	private static <T> void count(Map<T, Integer> count, Collection<T> c) {
        for (T t : c) {
            if (count.containsKey(t)) {
                count.put(t, count.get(t) + 1);
            }
            else {
                count.put(t, 1);
            }
        }
    }

    private static <T> int getSameCount(Map<T, Integer> countA, Map<T, Integer> countB, Collection<T> c) {
	    int counter = 0;
	    Set<T> set = new HashSet<>();
	    for (T t : c) {
	        if (!countA.containsKey(t) || !countB.containsKey(t)) {
	            continue;
            }
            if (set.contains(t)) {
                continue;
            }
            else {
                set.add(t);
            }
	        if (countA.get(t).equals(countB.get(t))) {
	            counter += 1;
            }
        }
        return counter;
    }

	
}
