/**
 * Test driver for the dual-heap Adaptable Priority Queue (AdvPQ).
 * Covers all ADT operations including edge cases, resizing, and exceptions.
 *
 * COMP 352 — Data Structures and Algorithms
 */
public class Tester {

    public static void main(String[] args) {

        // =====================================================================
        // Test 1: Create an empty APQ in MIN mode
        // =====================================================================
        printHeader(1, "Create an empty APQ in MIN mode");

        AdvPQ<Integer, String> apq = new AdvPQ<>(true);

        System.out.println("APQ created with startInMinMode = true");
        System.out.println(apq);

        // =====================================================================
        // Test 2: Check state()
        // =====================================================================
        printHeader(2, "Verify state() reports MIN");

        System.out.println("state() → " + apq.state());
        assert apq.state().equals("MIN") : "Expected MIN";
        System.out.println("PASS: state is MIN");

        // =====================================================================
        // Test 3: Check isEmpty() and size() on an empty APQ
        // =====================================================================
        printHeader(3, "isEmpty() and size() on empty APQ");

        System.out.println("isEmpty() → " + apq.isEmpty());
        System.out.println("size()    → " + apq.size());
        assert apq.isEmpty()    : "Should be empty";
        assert apq.size() == 0  : "Size should be 0";
        System.out.println("PASS: APQ is empty with size 0");

        // =====================================================================
        // Test 4: Basic inserts — 5 elements in MIN mode
        // =====================================================================
        printHeader(4, "Basic insert — 5 elements in MIN mode");

        System.out.println("Inserting: 30, 10, 50, 20, 40");
        Entry<Integer, String> e1 = apq.insert(30, "thirty");
        Entry<Integer, String> e2 = apq.insert(10, "ten");
        Entry<Integer, String> e3 = apq.insert(50, "fifty");
        Entry<Integer, String> e4 = apq.insert(20, "twenty");
        Entry<Integer, String> e5 = apq.insert(40, "forty");

        System.out.println("size() after 5 inserts → " + apq.size());
        System.out.println(apq);
        assert apq.size() == 5 : "Size should be 5";
        System.out.println("PASS: 5 entries inserted");

        // =====================================================================
        // Test 5: top() in MIN mode
        // =====================================================================
        printHeader(5, "top() in MIN mode — expect key 10");

        Entry<Integer, String> topEntry = apq.top();
        System.out.println("top() → " + topEntry);
        assert topEntry.getKey().equals(10) : "Top should be 10";
        System.out.println("PASS: smallest key (10) is at top in MIN mode");

        // =====================================================================
        // Test 6: removeTop() in MIN mode
        // =====================================================================
        printHeader(6, "removeTop() in MIN mode — removes key 10");

        Entry<Integer, String> removed = apq.removeTop();
        System.out.println("removeTop() → " + removed);
        System.out.println("size() after removeTop → " + apq.size());
        System.out.println(apq);
        assert removed.getKey().equals(10) : "Should have removed key 10";
        assert apq.size() == 4             : "Size should be 4";
        System.out.println("PASS: key 10 removed; new top should be 20");
        System.out.println("Confirming new top → " + apq.top());

        // =====================================================================
        // Test 7: replaceKey() — key decreases → causes UP-HEAP in min-heap
        // =====================================================================
        printHeader(7, "replaceKey() — key decreases (up-heap in MIN heap)");

        System.out.println("Entry e3 before replace: " + e3);
        System.out.println("Replacing key of e3: 50 → 5");
        Integer oldKey = apq.replaceKey(e3, 5);
        System.out.println("Old key returned: " + oldKey);
        System.out.println("e3 after replace: " + e3);
        System.out.println(apq);
        System.out.println("top() now → " + apq.top());
        assert apq.top().getKey().equals(5) : "Top should now be 5";
        System.out.println("PASS: key 5 bubbled to the top of MIN heap");

        // =====================================================================
        // Test 8: replaceKey() — key increases → causes DOWN-HEAP in min-heap
        // =====================================================================
        printHeader(8, "replaceKey() — key increases (down-heap in MIN heap)");

        System.out.println("Entry e3 current key: " + e3.getKey());
        System.out.println("Replacing key of e3: 5 → 60");
        oldKey = apq.replaceKey(e3, 60);
        System.out.println("Old key returned: " + oldKey);
        System.out.println("e3 after replace: " + e3);
        System.out.println(apq);
        System.out.println("top() now → " + apq.top());
        assert !apq.top().getKey().equals(60) : "60 should NOT be at top";
        System.out.println("PASS: key 60 sifted down from root in MIN heap");

        // =====================================================================
        // Test 9: replaceValue() — value changes, heap order unchanged
        // =====================================================================
        printHeader(9, "replaceValue() — change value without changing heap order");

        System.out.println("Entry e1 before: " + e1);
        System.out.println("Replacing value of e1: \"thirty\" → \"THIRTY-UPDATED\"");
        String oldValue = apq.replaceValue(e1, "THIRTY-UPDATED");
        System.out.println("Old value returned: " + oldValue);
        System.out.println("e1 after replace: " + e1);
        System.out.println(apq);
        assert e1.getValue().equals("THIRTY-UPDATED") : "Value should be updated";
        System.out.println("PASS: value updated; heap order unchanged");

        // =====================================================================
        // Test 10: remove(e) for a mid-heap entry (not the root, not the last)
        // =====================================================================
        printHeader(10, "remove(e) for a middle entry (e4, key=20)");

        System.out.println("Current APQ: " + apq);
        System.out.println("Removing e4 (key=20) from the middle of the heap...");
        Entry<Integer, String> removedMid = apq.remove(e4);
        System.out.println("Removed: " + removedMid);
        System.out.println("size() after remove → " + apq.size());
        System.out.println(apq);
        assert removedMid.getKey().equals(20) : "Should have removed key 20";
        System.out.println("PASS: middle entry removed; heaps restructured");

        // =====================================================================
        // Test 11: remove(e) for a leaf / last-type entry
        // =====================================================================
        printHeader(11, "remove(e) for a leaf/last entry (e5, key=40)");

        System.out.println("Current APQ: " + apq);
        System.out.println("Removing e5 (key=40)...");
        Entry<Integer, String> removedLeaf = apq.remove(e5);
        System.out.println("Removed: " + removedLeaf);
        System.out.println("size() after remove → " + apq.size());
        System.out.println(apq);
        assert removedLeaf.getKey().equals(40) : "Should have removed key 40";
        System.out.println("PASS: leaf-like entry removed correctly");

        // =====================================================================
        // Test 12: Trigger automatic array resizing by inserting many elements
        //          Default capacity is 16; we insert 20+ to force resize.
        // =====================================================================
        printHeader(12, "Automatic array extension — insert 25 elements");

        AdvPQ<Integer, String> bigApq = new AdvPQ<>(true);
        System.out.println("Inserting 25 elements (keys 1–25 in shuffled order)...");

        int[] keys = {
            15, 3, 22, 8, 19, 1, 25, 11, 17, 6,
            24, 13, 4, 20, 9, 2, 18, 7, 23, 16,
            5, 21, 10, 14, 12
        };
        Entry<Integer, String>[] bigEntries = (Entry<Integer, String>[]) new Entry[keys.length];
        for (int i = 0; i < keys.length; i++) {
            bigEntries[i] = bigApq.insert(keys[i], "val-" + keys[i]);
        }

        System.out.println("size() after 25 inserts → " + bigApq.size());
        System.out.println("top() (should be 1)    → " + bigApq.top());
        assert bigApq.size() == 25        : "Size should be 25";
        assert bigApq.top().getKey() == 1 : "Min should be 1";
        System.out.println(bigApq);
        System.out.println("PASS: 25 elements inserted; automatic resize occurred");

        // =====================================================================
        // Test 13: peekAt(1) — should equal top()
        // =====================================================================
        printHeader(13, "peekAt(1) — should match top()");

        Entry<Integer, String> peek1 = bigApq.peekAt(1);
        System.out.println("peekAt(1) → " + peek1);
        System.out.println("top()     → " + bigApq.top());
        assert peek1 == bigApq.top() : "peekAt(1) should be same object as top()";
        System.out.println("PASS: peekAt(1) matches top()");

        // =====================================================================
        // Test 14: peekAt(3) — 3rd smallest element
        // =====================================================================
        printHeader(14, "peekAt(3) — 3rd smallest in MIN mode (should be key 3)");

        Entry<Integer, String> peek3 = bigApq.peekAt(3);
        System.out.println("peekAt(3) → " + peek3);
        assert peek3.getKey() == 3 : "3rd smallest should be key 3";
        System.out.println("PASS: peekAt(3) returned key 3");

        // =====================================================================
        // Test 15: peekAt(size) — last (largest) element in MIN mode
        // =====================================================================
        printHeader(15, "peekAt(size) — largest element in MIN mode (should be key 25)");

        Entry<Integer, String> peekLast = bigApq.peekAt(bigApq.size());
        System.out.println("peekAt(" + bigApq.size() + ") → " + peekLast);
        assert peekLast.getKey() == 25 : "Last in MIN mode should be key 25";
        System.out.println("APQ unmodified — size still: " + bigApq.size());
        System.out.println("PASS: peekAt(size) returned the largest element");

        // =====================================================================
        // Test 16: toggle() — switch from MIN to MAX
        // =====================================================================
        printHeader(16, "toggle() — switch from MIN to MAX mode");

        System.out.println("State before toggle: " + bigApq.state());
        bigApq.toggle();
        System.out.println("State after  toggle: " + bigApq.state());
        assert bigApq.state().equals("MAX") : "State should be MAX";
        System.out.println("PASS: toggle switched to MAX in O(1)");

        // =====================================================================
        // Test 17: state() after toggle
        // =====================================================================
        printHeader(17, "state() confirms MAX after toggle");

        System.out.println("state() → " + bigApq.state());
        System.out.println("PASS: state() correctly reports MAX");

        // =====================================================================
        // Test 18: top() now follows MAX behavior — expect key 25
        // =====================================================================
        printHeader(18, "top() in MAX mode — expect key 25");

        Entry<Integer, String> maxTop = bigApq.top();
        System.out.println("top() in MAX mode → " + maxTop);
        assert maxTop.getKey() == 25 : "Max top should be key 25";
        System.out.println("PASS: top() returns maximum key (25) in MAX mode");

        // =====================================================================
        // Test 19: removeTop() in MAX mode — removes key 25
        // =====================================================================
        printHeader(19, "removeTop() in MAX mode — removes key 25");

        Entry<Integer, String> removedMax = bigApq.removeTop();
        System.out.println("removeTop() → " + removedMax);
        System.out.println("size() after removeTop → " + bigApq.size());
        System.out.println("new top() in MAX mode  → " + bigApq.top());
        assert removedMax.getKey() == 25 : "Should have removed key 25";
        assert bigApq.size() == 24       : "Size should be 24";
        System.out.println("PASS: key 25 removed; next largest is now at top");

        // =====================================================================
        // Test 20: replaceKey() while in MAX mode
        //          Lower a key — it should sink in max-heap (down-heap)
        // =====================================================================
        printHeader(20, "replaceKey() in MAX mode — key decreases (down-heap in MAX)");

        Entry<Integer, String> targetEntry = bigEntries[0]; // originally key=15
        System.out.println("Entry before replace: " + targetEntry);
        System.out.println("Replacing key " + targetEntry.getKey() + " → 0 (very small)");
        Integer old15 = bigApq.replaceKey(targetEntry, 0);
        System.out.println("Old key: " + old15);
        System.out.println("Entry after replace: " + targetEntry);
        System.out.println("top() in MAX mode → " + bigApq.top());
        assert !bigApq.top().getKey().equals(0) : "0 should NOT be at top in MAX mode";
        System.out.println("PASS: key 0 sank in max-heap; max element still at top");

        // =====================================================================
        // Test 21: toggle back to MIN mode
        // =====================================================================
        printHeader(21, "toggle() back to MIN mode");

        System.out.println("State before: " + bigApq.state());
        bigApq.toggle();
        System.out.println("State after : " + bigApq.state());
        System.out.println("top() in MIN mode → " + bigApq.top());
        assert bigApq.state().equals("MIN") : "Should be back in MIN";
        assert bigApq.top().getKey() == 0   : "New min should be key 0";
        System.out.println("PASS: toggled back to MIN; key 0 is now at top");

        // =====================================================================
        // Test 22: merge(other) — create a second APQ and merge it into bigApq
        // =====================================================================
        printHeader(22, "merge(other) — merge a second APQ into bigApq");

        AdvPQ<Integer, String> other = new AdvPQ<>(false); // starts in MAX mode
        System.out.println("Second APQ (other) created in MAX mode.");
        other.insert(100, "hundred");
        other.insert(50,  "fifty");
        other.insert(75,  "seventy-five");
        System.out.println("other APQ: " + other);
        System.out.println("bigApq size before merge: " + bigApq.size());
        System.out.println("other  size before merge: " + other.size());

        bigApq.merge(other);

        System.out.println("bigApq size after  merge: " + bigApq.size());
        System.out.println(bigApq);
        assert bigApq.size() == 27 : "After merge size should be 24 + 3 = 27";
        System.out.println("PASS: merge completed; all entries from other now in bigApq");

        // =====================================================================
        // Test 23: Merge preserves current mode of the primary APQ
        // =====================================================================
        printHeader(23, "Merge preserves current mode of primary APQ");

        System.out.println("bigApq.state() after merge → " + bigApq.state());
        assert bigApq.state().equals("MIN") : "Mode should still be MIN";
        System.out.println("PASS: mode is still MIN (other's MAX mode was not applied)");

        // =====================================================================
        // Test 24: Entries from merged APQ are present — new top should be 0
        // =====================================================================
        printHeader(24, "Verify merged entries are present and heap order is valid");

        System.out.println("top() after merge (MIN mode) → " + bigApq.top());
        assert bigApq.top().getKey() == 0 : "Min after merge should still be 0";
        System.out.println("Draining 5 elements to confirm merge data is accessible:");
        for (int i = 0; i < 5; i++) {
            Entry<Integer, String> drained = bigApq.removeTop();
            System.out.println("  removeTop() → " + drained);
        }
        System.out.println("size() after draining 5 → " + bigApq.size());
        System.out.println("PASS: merged entries are accessible in correct order");

        // =====================================================================
        // Test 25: peekAt() out-of-bounds — exception expected
        // =====================================================================
        printHeader(25, "peekAt() out-of-bounds — expect IllegalArgumentException");

        System.out.println("Attempting peekAt(0)  (below valid range)...");
        try {
            bigApq.peekAt(0);
            System.out.println("FAIL: No exception thrown for peekAt(0)");
        } catch (IllegalArgumentException ex) {
            System.out.println("Exception caught (peekAt(0)): " + ex.getMessage());
            System.out.println("PASS: IllegalArgumentException thrown for rank 0");
        }

        System.out.println();
        System.out.println("Attempting peekAt(size + 1)  (above valid range)...");
        int tooBig = bigApq.size() + 1;
        try {
            bigApq.peekAt(tooBig);
            System.out.println("FAIL: No exception thrown for peekAt(" + tooBig + ")");
        } catch (IllegalArgumentException ex) {
            System.out.println("Exception caught (peekAt(" + tooBig + ")): " + ex.getMessage());
            System.out.println("PASS: IllegalArgumentException thrown for out-of-bounds rank");
        }

        // =====================================================================
        // Test 26: remove(e) on an already-removed entry — exception expected
        // =====================================================================
        printHeader(26, "remove(e) on an already-removed entry — expect exception");

        System.out.println("Attempting to remove already-removed entry " + removed + "...");
        try {
            bigApq.remove(removed);
            System.out.println("FAIL: No exception thrown");
        } catch (IllegalArgumentException ex) {
            System.out.println("Exception caught: " + ex.getMessage());
            System.out.println("PASS: Exception thrown for already-removed entry");
        }

        System.out.println();
        AdvPQ<Integer, String> foreignApq = new AdvPQ<>(true);
        Entry<Integer, String> foreignEntry = foreignApq.insert(999, "foreign");
        System.out.println("Attempting to remove a foreign entry " + foreignEntry +
                           " from bigApq...");
        try {
            bigApq.remove(foreignEntry);
            System.out.println("FAIL: No exception thrown");
        } catch (IllegalArgumentException ex) {
            System.out.println("Exception caught: " + ex.getMessage());
            System.out.println("PASS: Exception thrown for entry belonging to a different APQ");
        }

        // =====================================================================
        // Test 27: removeTop() on an empty APQ — exception expected
        // =====================================================================
        printHeader(27, "removeTop() on an empty APQ — expect NoSuchElementException");

        AdvPQ<Integer, String> emptyApq = new AdvPQ<>(true);
        System.out.println("Attempting removeTop() on a freshly created empty APQ...");
        try {
            emptyApq.removeTop();
            System.out.println("FAIL: No exception thrown");
        } catch (java.util.NoSuchElementException ex) {
            System.out.println("Exception caught: " + ex.getMessage());
            System.out.println("PASS: NoSuchElementException thrown on empty APQ");
        }

        // =====================================================================
        // Test 28: Drain remaining elements — verify heap maintenance throughout
        // =====================================================================
        printHeader(28, "Drain all remaining elements in MIN mode — verify sorted order");

        System.out.println("Current state: " + bigApq.state() +
                           ", size: " + bigApq.size());
        System.out.println("Draining all elements (should come out in ascending order):");

        int prevKey = Integer.MIN_VALUE;
        boolean sortedOk = true;
        int drainCount = 0;

        while (!bigApq.isEmpty()) {
            Entry<Integer, String> drainEntry = bigApq.removeTop();
            System.out.println("  removeTop() → " + drainEntry);
            if (drainEntry.getKey() < prevKey) {
                sortedOk = false;
                System.out.println("  *** ORDER VIOLATION at key " + drainEntry.getKey() + " ***");
            }
            prevKey = drainEntry.getKey();
            drainCount++;
        }

        System.out.println("Total elements drained: " + drainCount);
        System.out.println("Heap order maintained throughout: " + sortedOk);
        assert sortedOk          : "Elements should come out in non-decreasing order";
        assert bigApq.isEmpty()  : "APQ should be empty after draining";
        System.out.println("PASS: all elements removed in sorted order");

        // =====================================================================
        // Test 29: isEmpty() and size() after draining everything
        // =====================================================================
        printHeader(29, "isEmpty() and size() after full drain");

        System.out.println("isEmpty() → " + bigApq.isEmpty());
        System.out.println("size()    → " + bigApq.size());
        assert bigApq.isEmpty()   : "APQ should be empty";
        assert bigApq.size() == 0 : "Size should be 0";
        System.out.println("PASS: APQ correctly reports empty with size 0");

        // =====================================================================
        // Test 30: Reuse APQ after full drain — insert and verify
        // =====================================================================
        printHeader(30, "Reuse APQ after full drain — insert new elements");

        System.out.println("Inserting 3 new elements after APQ was fully drained...");
        Entry<Integer, String> r1 = bigApq.insert(7,  "seven");
        Entry<Integer, String> r2 = bigApq.insert(2,  "two");
        Entry<Integer, String> r3 = bigApq.insert(11, "eleven");

        System.out.println("size() → " + bigApq.size());
        System.out.println("top()  → " + bigApq.top());
        System.out.println(bigApq);
        assert bigApq.size() == 3   : "Size should be 3";
        assert bigApq.top().getKey() == 2 : "Min should be 2";
        System.out.println("PASS: APQ reused successfully after full drain");

        // =====================================================================
        // Test 31: replaceKey() — MAX mode, key increases → up-heap in max-heap
        // =====================================================================
        printHeader(31, "replaceKey() in MAX mode — key increases (up-heap in MAX)");

        bigApq.toggle();
        System.out.println("Toggled to: " + bigApq.state());
        System.out.println("top() in MAX mode → " + bigApq.top());  // should be 11

        // r1 has key=7.  Raise it to 100 → should bubble to the top.
        System.out.println("Entry r1 before: " + r1 + "  (key=7)");
        System.out.println("Replacing key of r1: 7 → 100");
        Integer oldR1 = bigApq.replaceKey(r1, 100);
        System.out.println("Old key: " + oldR1);
        System.out.println("top() after replace → " + bigApq.top());
        assert bigApq.top().getKey() == 100 : "100 should be at top in MAX mode";
        System.out.println("PASS: key 100 bubbled to top of MAX heap");

        // =====================================================================
        // Test 32: isValid() check on live vs. removed entries
        // =====================================================================
        printHeader(32, "isValid() on live and removed entries");

        System.out.println("r1.isValid() (live entry)    → " + r1.isValid());
        System.out.println("removed.isValid() (removed)  → " + removed.isValid());
        assert  r1.isValid()      : "r1 should be valid (live)";
        assert !removed.isValid() : "removed should not be valid (already removed)";
        System.out.println("PASS: isValid() correctly distinguishes live vs. removed entries");

        // =====================================================================
        // Test 33: merge(null) and merge(empty) — both should be no-ops
        // =====================================================================
        printHeader(33, "merge(null) and merge(empty) are silent no-ops");

        int sizeBefore = bigApq.size();
        System.out.println("size before merge(null)  → " + sizeBefore);
        bigApq.merge(null);
        System.out.println("size after  merge(null)  → " + bigApq.size());
        assert bigApq.size() == sizeBefore : "Size should not change after merge(null)";

        AdvPQ<Integer, String> emptyOther = new AdvPQ<>(true);
        bigApq.merge(emptyOther);
        System.out.println("size after  merge(empty) → " + bigApq.size());
        assert bigApq.size() == sizeBefore : "Size should not change after merge(empty)";
        System.out.println("PASS: merge(null) and merge(empty) are no-ops");

        // =====================================================================
        // Test 34: many up-heap and down-heap operations via sequential inserts
        //          and a replaceKey sweep across many entries
        // =====================================================================
        printHeader(34, "Stress test — many replaceKey calls to exercise up/down-heap");

        AdvPQ<Integer, String> stress = new AdvPQ<>(true);
        Entry<Integer, String>[] stressEntries = (Entry<Integer, String>[]) new Entry[10];
        int[] stressKeys = {50, 30, 70, 20, 40, 60, 80, 10, 25, 55};
        System.out.println("Inserting 10 entries with keys: ");
        for (int i = 0; i < stressKeys.length; i++) {
            stressEntries[i] = stress.insert(stressKeys[i], "s" + stressKeys[i]);
            System.out.print(stressKeys[i] + " ");
        }
        System.out.println();
        System.out.println(stress);

        System.out.println("Reversing all keys (10→80, 25→70, ...): ");
        int[] newKeys = {5, 75, 35, 85, 15, 45, 25, 95, 65, 55};
        for (int i = 0; i < stressEntries.length; i++) {
            stress.replaceKey(stressEntries[i], newKeys[i]);
        }
        System.out.println(stress);
        System.out.println("top() after all replaceKey calls → " + stress.top());
        assert stress.top().getKey() == 5 : "Min after replacements should be 5";
        System.out.println("Draining to verify sorted order:");

        int prev = Integer.MIN_VALUE;
        boolean stressOk = true;
        while (!stress.isEmpty()) {
            int k = stress.removeTop().getKey();
            System.out.print(k + " ");
            if (k < prev) stressOk = false;
            prev = k;
        }
        System.out.println();
        assert stressOk : "Stress drain should be in sorted order";
        System.out.println("PASS: all elements came out in sorted order after many replaceKey calls");

        // =====================================================================
        // Summary
        // =====================================================================
        System.out.println();
        System.out.println("=".repeat(60));
        System.out.println("  ALL TESTS COMPLETED SUCCESSFULLY");
        System.out.println("=".repeat(60));
    }

    // =========================================================================
    // Utility helper
    // =========================================================================

    private static void printHeader(int number, String description) {
        System.out.println();
        System.out.println("=".repeat(60));
        System.out.printf("  Test %d: %s%n", number, description);
        System.out.println("=".repeat(60));
    }
}