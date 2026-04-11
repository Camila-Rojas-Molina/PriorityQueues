/**
 * A single entry in a dual-heap APQ, storing a key, value, and its
 * position in both heaps (minIndex / maxIndex). -1 means not inserted.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class Entry<K, V> {

    // =========================================================================
    // Fields
    // =========================================================================

    private K key;
    private V value;
    private int minIndex;
    private int maxIndex;

    // =========================================================================
    // Constructor
    // =========================================================================

    public Entry(K key, V value) {
        this.key      = key;
        this.value    = value;
        this.minIndex = -1;
        this.maxIndex = -1;
    }

    // =========================================================================
    // Key accessors
    // =========================================================================

    public K getKey()        { return key; }
    public void setKey(K key) { this.key = key; }

    // =========================================================================
    // Value accessors
    // =========================================================================

    public V getValue()           { return value; }
    public void setValue(V value) { this.value = value; }

    // =========================================================================
    // Min-heap index accessors
    // =========================================================================

    public int getMinIndex()             { return minIndex; }
    public void setMinIndex(int minIndex) { this.minIndex = minIndex; }

    // =========================================================================
    // Max-heap index accessors
    // =========================================================================

    public int getMaxIndex()             { return maxIndex; }
    public void setMaxIndex(int maxIndex) { this.maxIndex = maxIndex; }

    // =========================================================================
    // Utility methods
    // =========================================================================

    public boolean isValid() { return minIndex >= 0 && maxIndex >= 0; }

    @Override
    public String toString() { return "(" + key + ", " + value + ")"; }

}