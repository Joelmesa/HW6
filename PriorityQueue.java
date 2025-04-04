
/******************************************************************
 *
 *   Joel Mesa / Comp272
 *
 *   Note, additional comments provided throughout this source code
 *   is for educational purposes
 *
 ********************************************************************/

import java.util.ArrayList;
import java.util.Comparator;

class PriorityQueue<E, P> {

    private static final int DEFAULT_CAPACITY = 10;

    final Comparator<P> comparator;
    final ArrayList<Node> tree;

    public PriorityQueue() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public PriorityQueue(int capacity) {
        this(capacity, (a, b) -> ((Comparable<P>) a).compareTo(b));
    }

    public PriorityQueue(int capacity, Comparator<P> comparator) {
        tree = new ArrayList<>(capacity);
        this.comparator = comparator;
    }

    public int size()               { return tree.size(); }
    public boolean isEmpty()        { return tree.size() == 0; }
    public void clear()             { tree.clear(); }
    public Node offer(E e, P p)     { return add(e, p); }

    public Node peek() {
        if (size() == 0) return null;
        return tree.get(0);
    }

    // ✅ Working add method
    public Node add(E e, P priority) {
        Node newNode = new Node(e, priority, tree.size());
        tree.add(newNode);
        pullUp(tree.size() - 1);
        return newNode;
    }

    // ✅ Working contains method
    public boolean contains(E e) {
        for (Node node : tree) {
            if (!node.removed && node.value().equals(e)) {
                return true;
            }
        }
        return false;
    }

    public Node remove() {
        if (tree.size() == 0) {
            throw new IllegalStateException("PriorityQueue is empty");
        }
        return poll();
    }

    public Node poll() {
        if (tree.size() == 0) return null;

        if (tree.size() == 1) {
            final Node removedNode = tree.remove(0);
            removedNode.markRemoved();
            return removedNode;
        } else {
            Node head = tree.get(0);
            head.markRemoved();
            final Node nodeToMoveToHead = tree.remove(tree.size() - 1);
            nodeToMoveToHead.idx = 0;
            tree.set(0, nodeToMoveToHead);
            pushDown(0);
            return head;
        }
    }

    private void pushDown(int i) {
        while ((leftChild(i) < size() && compare(tree.get(leftChild(i)).priority, tree.get(i).priority) < 0) ||
                (rightChild(i) < size() && compare(tree.get(rightChild(i)).priority, tree.get(i).priority) < 0)) {
            int leftChildIdx = leftChild(i);
            int rightChildIdx = rightChild(i);
            if (rightChildIdx >= size() ||
                    compare(tree.get(leftChildIdx).priority, tree.get(rightChildIdx).priority) < 0) {
                swap(i, leftChildIdx);
                i = leftChildIdx;
            } else {
                swap(i, rightChildIdx);
                i = rightChildIdx;
            }
        }
    }

    private void pullUp(int i) {
        while (i != 0 && compare(tree.get(parent(i)).priority, tree.get(i).priority) > 0) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    int leftChild(int i)          { return 2 * i + 1; }
    int rightChild(int i)         { return 2 * i + 2; }
    int parent(int i)             { return (i - 1) / 2; }

    private int compare(P a, P b) { return comparator.compare(a, b); }

    void swap(int idx1, int idx2) {
        Node node1 = tree.get(idx1);
        Node node2 = tree.get(idx2);
        node1.idx = idx2;
        node2.idx = idx1;
        tree.set(idx1, node2);
        tree.set(idx2, node1);
    }

    public ArrayList<E> toArray() {
        ArrayList<E> array = new ArrayList<>();
        for (int i = 0; i < size(); i++) {
            array.add(tree.get(i).value());
        }
        return array;
    }

    public void printPriorityQueue() {
        System.out.print("Priority Queue: [ ");
        for (int i = 0; i < size(); i++) {
            Node node = tree.get(i);
            System.out.print("(" + node.value + "," + node.priority + "), ");
        }
        System.out.println("]");
    }

    // ✅ Inner Node class with no additional type parameters
    public class Node {
        public E value;
        public P priority;
        public int idx;
        public boolean removed = false;

        public Node(E value, P priority, int idx) {
            this.value = value;
            this.priority = priority;
            this.idx = idx;
        }

        void markRemoved()          { removed = true; }
        public E value()            { return value; }
        public P priority()         { return priority; }
        public boolean isValid()    { return !removed; }

        public void changePriority(P newPriority) {
            checkNodeValidity();
            if (compare(newPriority, priority) < 0) {
                priority = newPriority;
                pullUp(idx);
            } else if (compare(newPriority, priority) > 0) {
                priority = newPriority;
                pushDown(idx);
            }
        }

        public void remove() {
            checkNodeValidity();

            if (tree.size() == 1) {
                tree.remove(idx);
                markRemoved();
            } else if (idx == tree.size() - 1) {
                markRemoved();
                tree.remove(idx);
            } else {
                markRemoved();
                final Node nodeToMoveToThisIdx = tree.remove(tree.size() - 1);
                nodeToMoveToThisIdx.idx = idx;
                tree.set(idx, nodeToMoveToThisIdx);
                if (compare(tree.get(parent(idx)).priority, nodeToMoveToThisIdx.priority) > 0) {
                    pullUp(idx);
                } else {
                    pushDown(idx);
                }
            }
        }

        private void checkNodeValidity() {
            if (removed) {
                throw new IllegalStateException("node is no longer part of heap");
            }
        }
    }
}
