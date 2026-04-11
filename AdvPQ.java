/**
--------------------------------------------------------------------------
Assignment 3 - ADV PQ
Name and ID: Camila Rojas (40321494) Section S
Name and ID: Daniel Tehrani (40100248) Section X
COMP 352 Winter 2026
/---------------------------------------------------------------------------
*/

/**
 * Dual-heap Adaptable Priority Queue maintaining a min-heap and max-heap
 * over the same entries. Supports O(log n) arbitrary removal and key replacement
 * via per-entry index fields, O(1) mode toggle, and O(n+m) merge.
 *
 * @param <K> key type, must implement Comparable<K>
 * @param <V> value type
 */
public class AdvPQ<K extends Comparable<K>, V> {

    // =========================================================================
    // Fields
    // =========================================================================

    private Entry<K, V>[] minHeap;
    private Entry<K, V>[] maxHeap;
    private int size;
    private int capacity;
    private boolean isMinMode;

    private static final int DEFAULT_CAPACITY = 16;

    // =========================================================================
    // Constructors
    // =========================================================================

    @SuppressWarnings("unchecked")
    public AdvPQ(boolean startInMinMode) {
        this.capacity  = DEFAULT_CAPACITY;
        this.size      = 0;
        this.isMinMode = startInMinMode;
        this.minHeap   = (Entry<K, V>[]) new Entry[capacity];
        this.maxHeap   = (Entry<K, V>[]) new Entry[capacity];
    }

    @SuppressWarnings("unchecked")
    public AdvPQ(boolean startInMinMode, int initialCapacity) {
        if (initialCapacity <= 0)
            throw new IllegalArgumentException("Initial capacity must be positive.");
        this.capacity  = initialCapacity;
        this.size      = 0;
        this.isMinMode = startInMinMode;
        this.minHeap   = (Entry<K, V>[]) new Entry[capacity];
        this.maxHeap   = (Entry<K, V>[]) new Entry[capacity];
    }

    // =========================================================================
    // Basic state queries
    // =========================================================================

    public boolean isEmpty() { return size == 0; }

    public int size() { return size; }

    public String state() { return isMinMode ? "MIN" : "MAX"; }

    public void toggle() { isMinMode = !isMinMode; }

    // =========================================================================
    // insert
    // =========================================================================

    public Entry<K, V> insert(K key, V value) {
        if (key == null)
            throw new IllegalArgumentException("Key must not be null.");

        ensureCapacity();

        Entry<K, V> e = new Entry<>(key, value);

        minHeap[size] = e;
        maxHeap[size] = e;
        e.setMinIndex(size);
        e.setMaxIndex(size);
        size++;

        upHeapMin(e.getMinIndex());
        upHeapMax(e.getMaxIndex());

        return e;
    }

    // =========================================================================
    // top
    // =========================================================================

    public Entry<K, V> top() {
        if (isEmpty()) return null;
        return isMinMode ? minHeap[0] : maxHeap[0];
    }

    // =========================================================================
    // removeTop
    // =========================================================================

    public Entry<K, V> removeTop() {
        if (isEmpty())
            throw new java.util.NoSuchElementException("APQ is empty.");
        return remove(isMinMode ? minHeap[0] : maxHeap[0]);
    }

    // =========================================================================
    // remove(e)
    // =========================================================================

    public Entry<K, V> remove(Entry<K, V> e) {
        validateEntry(e);

        int mi   = e.getMinIndex();
        int xi   = e.getMaxIndex();
        int last = size - 1;

        // Remove from min-heap
        if (mi == last) {
            minHeap[last] = null;
        } else {
            Entry<K, V> replacement = minHeap[last];
            minHeap[mi] = replacement;
            replacement.setMinIndex(mi);
            minHeap[last] = null;
            upHeapMin(mi);
            downHeapMin(replacement.getMinIndex());
        }

        // Remove from max-heap (xi unaffected by min-heap removal above)
        if (xi == last) {
            maxHeap[last] = null;
        } else {
            Entry<K, V> replacement = maxHeap[last];
            maxHeap[xi] = replacement;
            replacement.setMaxIndex(xi);
            maxHeap[last] = null;
            upHeapMax(xi);
            downHeapMax(replacement.getMaxIndex());
        }

        size--;
        e.setMinIndex(-1);
        e.setMaxIndex(-1);

        return e;
    }

    // =========================================================================
    // replaceKey
    // =========================================================================

    public K replaceKey(Entry<K, V> e, K newKey) {
        if (newKey == null)
            throw new IllegalArgumentException("New key must not be null.");
        validateEntry(e);

        K oldKey = e.getKey();
        e.setKey(newKey);

        upHeapMin(e.getMinIndex());
        downHeapMin(e.getMinIndex());

        upHeapMax(e.getMaxIndex());
        downHeapMax(e.getMaxIndex());

        return oldKey;
    }

    // =========================================================================
    // replaceValue
    // =========================================================================

    public V replaceValue(Entry<K, V> e, V newValue) {
        validateEntry(e);
        V oldValue = e.getValue();
        e.setValue(newValue);
        return oldValue;
    }

    // =========================================================================
    // peekAt(n)
    // =========================================================================

    /** Returns the n-th ranked entry (1-based) in the current mode without removal. O(n log n). */
    public Entry<K, V> peekAt(int n) {
        if (n < 1 || n > size)
            throw new IllegalArgumentException(
                "Rank " + n + " is out of bounds (size=" + size + ").");

        Entry<K, V>[] active = isMinMode ? minHeap : maxHeap;
        CandidateHeap cHeap = new CandidateHeap(2 * n + 8, isMinMode, active);

        cHeap.insert(0);

        int best = 0;
        for (int rank = 0; rank < n; rank++) {
            best = cHeap.removeTop();
            int left  = leftChild(best);
            int right = rightChild(best);
            if (left  < size) cHeap.insert(left);
            if (right < size) cHeap.insert(right);
        }

        return active[best];
    }

    // =========================================================================
    // merge
    // =========================================================================

    /** Merges all entries from {@code other} into this APQ using Floyd's heapify. O(n+m). */
    public void merge(AdvPQ<K, V> other) {
        if (other == null || other.isEmpty()) return;

        int combined = this.size + other.size;

        while (combined > this.capacity) growArrays();

        for (int i = 0; i < other.size; i++) {
            Entry<K, V> orig = other.minHeap[i];
            Entry<K, V> copy = new Entry<>(orig.getKey(), orig.getValue());

            int slot = this.size + i;
            this.minHeap[slot] = copy;
            this.maxHeap[slot] = copy;
            copy.setMinIndex(slot);
            copy.setMaxIndex(slot);
        }

        this.size = combined;

        buildMinHeap();
        buildMaxHeap();
    }

    // =========================================================================
    // toString
    // =========================================================================

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AdvPQ[mode=").append(state())
          .append(", size=").append(size).append("]\n");

        sb.append("  MinHeap: [");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(minHeap[i].getKey());
        }
        sb.append("]\n");

        sb.append("  MaxHeap: [");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(maxHeap[i].getKey());
        }
        sb.append("]");

        return sb.toString();
    }

    // =========================================================================
    // Private helpers — capacity management
    // =========================================================================

    private void ensureCapacity() {
        if (size >= capacity) growArrays();
    }

    @SuppressWarnings("unchecked")
    private void growArrays() {
        int newCap = capacity * 2;
        Entry<K, V>[] newMin = (Entry<K, V>[]) new Entry[newCap];
        Entry<K, V>[] newMax = (Entry<K, V>[]) new Entry[newCap];
        System.arraycopy(minHeap, 0, newMin, 0, size);
        System.arraycopy(maxHeap, 0, newMax, 0, size);
        minHeap  = newMin;
        maxHeap  = newMax;
        capacity = newCap;
    }

    // =========================================================================
    // Private helpers — heap index arithmetic
    // =========================================================================

    private static int parent(int i)     { return (i - 1) / 2; }
    private static int leftChild(int i)  { return 2 * i + 1;   }
    private static int rightChild(int i) { return 2 * i + 2;   }

    // =========================================================================
    // Private helpers — min-heap primitives
    // =========================================================================

    private void swapMin(int i, int j) {
        Entry<K, V> tmp = minHeap[i];
        minHeap[i] = minHeap[j];
        minHeap[j] = tmp;
        minHeap[i].setMinIndex(i);
        minHeap[j].setMinIndex(j);
    }

    private void upHeapMin(int i) {
        while (i > 0) {
            int p = parent(i);
            if (minHeap[i].getKey().compareTo(minHeap[p].getKey()) < 0) {
                swapMin(i, p);
                i = p;
            } else {
                break;
            }
        }
    }

    private void downHeapMin(int i) {
        while (true) {
            int smallest = i;
            int l = leftChild(i);
            int r = rightChild(i);

            if (l < size && minHeap[l].getKey().compareTo(minHeap[smallest].getKey()) < 0)
                smallest = l;
            if (r < size && minHeap[r].getKey().compareTo(minHeap[smallest].getKey()) < 0)
                smallest = r;

            if (smallest != i) {
                swapMin(i, smallest);
                i = smallest;
            } else {
                break;
            }
        }
    }

    // =========================================================================
    // Private helpers — max-heap primitives
    // =========================================================================

    private void swapMax(int i, int j) {
        Entry<K, V> tmp = maxHeap[i];
        maxHeap[i] = maxHeap[j];
        maxHeap[j] = tmp;
        maxHeap[i].setMaxIndex(i);
        maxHeap[j].setMaxIndex(j);
    }

    private void upHeapMax(int i) {
        while (i > 0) {
            int p = parent(i);
            if (maxHeap[i].getKey().compareTo(maxHeap[p].getKey()) > 0) {
                swapMax(i, p);
                i = p;
            } else {
                break;
            }
        }
    }

    private void downHeapMax(int i) {
        while (true) {
            int largest = i;
            int l = leftChild(i);
            int r = rightChild(i);

            if (l < size && maxHeap[l].getKey().compareTo(maxHeap[largest].getKey()) > 0)
                largest = l;
            if (r < size && maxHeap[r].getKey().compareTo(maxHeap[largest].getKey()) > 0)
                largest = r;

            if (largest != i) {
                swapMax(i, largest);
                i = largest;
            } else {
                break;
            }
        }
    }

    // =========================================================================
    // Private helpers — full heapify (used by merge)
    // =========================================================================

    private void buildMinHeap() {
        for (int i = 0; i < size; i++) minHeap[i].setMinIndex(i);
        for (int i = parent(size - 1); i >= 0; i--) downHeapMin(i);
    }

    private void buildMaxHeap() {
        for (int i = 0; i < size; i++) maxHeap[i].setMaxIndex(i);
        for (int i = parent(size - 1); i >= 0; i--) downHeapMax(i);
    }

    // =========================================================================
    // Private helpers — entry validation
    // =========================================================================

    private void validateEntry(Entry<K, V> e) {
        if (e == null)
            throw new IllegalArgumentException("Entry must not be null.");

        int mi = e.getMinIndex();
        int xi = e.getMaxIndex();

        if (mi < 0 || mi >= size)
            throw new IllegalArgumentException(
                "Entry has invalid minIndex=" + mi +
                " (size=" + size + "). It may have been removed or belong to a different APQ.");

        if (xi < 0 || xi >= size)
            throw new IllegalArgumentException(
                "Entry has invalid maxIndex=" + xi +
                " (size=" + size + "). It may have been removed or belong to a different APQ.");

        if (minHeap[mi] != e)
            throw new IllegalArgumentException(
                "minHeap[" + mi + "] does not reference this entry. " +
                "The entry may belong to a different APQ.");

        if (maxHeap[xi] != e)
            throw new IllegalArgumentException(
                "maxHeap[" + xi + "] does not reference this entry. " +
                "The entry may belong to a different APQ.");
    }

    // =========================================================================
    // Private inner class — auxiliary candidate heap for peekAt(n)
    // =========================================================================

    private class CandidateHeap {

        private int[]              data;
        private int                cSize;
        private final boolean      minOrder;
        private final Entry<K,V>[] active;

        @SuppressWarnings("unchecked")
        CandidateHeap(int initialCap, boolean minOrder, Entry<K, V>[] activeHeap) {
            this.data     = new int[Math.max(initialCap, 4)];
            this.cSize    = 0;
            this.minOrder = minOrder;
            this.active   = activeHeap;
        }

        void insert(int idx) {
            if (cSize >= data.length) {
                int[] bigger = new int[data.length * 2];
                System.arraycopy(data, 0, bigger, 0, cSize);
                data = bigger;
            }
            data[cSize] = idx;
            bubbleUp(cSize);
            cSize++;
        }

        int removeTop() {
            int top = data[0];
            cSize--;
            if (cSize > 0) {
                data[0] = data[cSize];
                siftDown(0);
            }
            return top;
        }

        private boolean hasPriority(int a, int b) {
            int cmp = active[a].getKey().compareTo(active[b].getKey());
            return minOrder ? (cmp < 0) : (cmp > 0);
        }

        private int cParent(int i) { return (i - 1) / 2; }
        private int cLeft(int i)   { return 2 * i + 1;   }
        private int cRight(int i)  { return 2 * i + 2;   }

        private void cSwap(int i, int j) {
            int tmp = data[i]; data[i] = data[j]; data[j] = tmp;
        }

        private void bubbleUp(int i) {
            while (i > 0) {
                int p = cParent(i);
                if (hasPriority(data[i], data[p])) {
                    cSwap(i, p);
                    i = p;
                } else {
                    break;
                }
            }
        }

        private void siftDown(int i) {
            while (true) {
                int best  = i;
                int left  = cLeft(i);
                int right = cRight(i);
                if (left  < cSize && hasPriority(data[left],  data[best])) best = left;
                if (right < cSize && hasPriority(data[right], data[best])) best = right;
                if (best != i) { cSwap(i, best); i = best; }
                else break;
            }
        }

    }

}
