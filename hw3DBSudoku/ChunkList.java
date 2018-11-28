import java.util.*;

/**
 * similar to Array, but divide the array into Chunk, so that the array modification operation
 * only affect one piece of Chunk instead of entire Array.
 * @param <T> generic type
 */
public class ChunkList<T> extends AbstractCollection<T> implements Iterable<T> {
    /**
     * defines the data unite in ChunkList
     */
    private class Chunk {
        public static final int ARRAY_SIZE = 8;
        // the element number of this Chunk instance
        private int size;
        private T[] array;
        private Chunk next;

        public Chunk() {
            array = (T[]) new Object[ARRAY_SIZE];
            size = 0;
            next = null;
        }

        public boolean isFull() {
            return size == ARRAY_SIZE;
        }
    }

    /**
     * defines the Iterator used for ChunkList
     */
    public class ChunkIterator implements Iterator<T> {
        private Chunk lastChunk, lastSecChunk, nextChunk;
        private int next;
        private boolean rmFlag;

        public ChunkIterator() {
            lastChunk = null;
            lastSecChunk = null;
            nextChunk = head;
            next = 0;
            rmFlag = false;
        }

        /**
         * @return true if there are elements to be iterated
         */
        @Override
        public boolean hasNext() {
//            return nextChunk == tail && next == tail.size;
//            return nextChunk != tail || next != tail.size;
            return nextChunk != null || next != 0;
        }

        /**
         * @return the next element
         */
        @Override
        public T next() {
            if (!hasNext()) {
                throw new java.lang.RuntimeException("has no next element");
            }
            // get the next element
            T nextE = nextChunk.array[next];
            // update variables
            // if next is the last element
            if (next == nextChunk.size - 1) {
                lastSecChunk = lastChunk;
                lastChunk = nextChunk;
                nextChunk = nextChunk.next;
                next = 0;
            }
            else { // next is not the last element
                next += 1;
            }
            rmFlag = true;
            return nextE;
        }

        // need to call next() first
        @Override
        public void remove() {
            if (!rmFlag) {
                throw new java.lang.RuntimeException("should call next() before remove()");
            }
            rmFlag = false;
            // if last element stays within the last Chunk, while next element stays on
            // nextChunk
            if (next == 0) {
                // if last Chunk contains only one element, just remove the Chunk
                if (lastChunk.size == 1) {
                    // if last Chunk is head
                    if (lastChunk == head) {
                        head = nextChunk;
//                        lastChunk = null;
                    }
                    // else last Chunk is not head
                    else {
                        lastSecChunk.next = nextChunk;
                        // if the removed lastChunk is tail
                        if (lastChunk == tail) {
                            tail = lastSecChunk;
                        }
                        lastChunk = lastSecChunk;
                    }
                }
                // else remove directly
                else {
                    lastChunk.array[lastChunk.size - 1] = null;
                    lastChunk.size -= 1;
                }
            }
            // else last element stays within the same Chunk as next, just remove it directly
            else {
//                nextChunk.array[next - 1] = null;
                System.arraycopy(nextChunk.array,next, nextChunk.array, next - 1, Chunk.ARRAY_SIZE - next);
                nextChunk.array[nextChunk.size - 1] = null;
                next -= 1;
                nextChunk.size -= 1;
            }
            size -= 1;
        }
    }

    Chunk head, tail;
    int size;

    public ChunkList() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * add an element t to the ChunkList
     * @param t the element to be added
     * @return true if succeed
     */
    @Override
    public boolean add(T t) {
        if (size == 0) {
            tail = new Chunk();
            head = tail;
        }
        else if (tail.isFull()) {
            Chunk tmp = new Chunk();
            tail.next = tmp;
            tail = tmp;
        }
        tail.array[tail.size] = t;
        tail.size += 1;
        size += 1;
        return true;
    }

    /**
     * @return an iterator of ChunkList, it fulfills hasNext(), next(), remove()
     */
    @Override
    public Iterator<T> iterator() {
        return new ChunkIterator();
    }

    /**
     * @return the number of elements stored in the ChunkList
     */
    @Override
    public int size() {
        return this.size;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        Chunk current = head;
        while (current != null) {
            for (int i = 0; i < current.size; i++) {
                buffer.append(current.array[i].toString());
            }
            current = current.next;
        }
        return buffer.toString();
    }
}
