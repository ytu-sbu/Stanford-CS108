/*
 HW1 Taboo problem class.
 Taboo encapsulates some rules about what objects
 may not follow other objects.
 (See handout).
*/

import java.util.*;

public class Taboo<T> {
    HashMap<T, Set<T>> rules;

	/**
	 * Constructs a new Taboo using the given rules (see handout.)
	 * @param rules rules for new Taboo
	 */
	public Taboo(List<T> rules) {
	    this.rules = new HashMap<>();
	    for (int i = 0; i < rules.size() - 1; i++) {
	        T key = rules.get(i);
	        T next = rules.get(i + 1);
	        if (key == null || next == null) {
	            continue;
            }
	        // rules not contains the key
	        if (!this.rules.containsKey(key)) {
	            Set<T> s = new HashSet<>();
	            // add next item
	            s.add(next);
	            this.rules.put(key, s);
            }
            // rules already contains the key
            else {
                this.rules.get(key).add(next);
            }
        }
	}
	
	/**
	 * Returns the set of elements which should not follow
	 * the given element.
	 * @param elem
	 * @return elements which should not follow the given element
	 */
	public Set<T> noFollow(T elem) {
	    if (!rules.containsKey(elem)) {
	        return Collections.EMPTY_SET;
        }
	    return rules.get(elem); // YOUR CODE HERE
	}
	
	/**
	 * Removes elements from the given list that
	 * violate the rules (see handout).
	 * @param list collection to reduce
	 */
	public void reduce(List<T> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            T key = list.get(i);
            if (!rules.containsKey(key)) {
                continue;
            }
            if (rules.get(key).contains(list.get(i + 1))) {
                list.remove(i + 1);
                i -= 1;
            }
        }
	}
}
