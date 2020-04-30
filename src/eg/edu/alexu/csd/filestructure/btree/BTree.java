package eg.edu.alexu.csd.filestructure.btree;

import javax.management.RuntimeErrorException;
import java.util.List;

public class BTree<K extends Comparable<K>, V> implements IBTree<K, V> {
    private IBTreeNode<K, V> root;
    private int t;

    public BTree(int t) {
        if (t <= 1) {
            throw new RuntimeErrorException(new Error("Degree must be greater than 1!"));
        }
        this.t = t;
        root = null;
    }


    public int getMinimumDegree() {
        return t;
    }

    public IBTreeNode<K, V> getRoot() {
        return root;
    }

    private IBTreeNode<K, V> getNode(K key) {
        IBTreeNode<K, V> current = root;
        while (true) {
            for (int i = 0; i < current.getKeys().size(); i++) {
                if (current.getKeys().get(i).compareTo(key) == 0) {
                    return current;
                }
            }
            if (current.isLeaf()) {
                return null;
            }
            current = current.getChildren().get(getInsertionLoc(current.getKeys(), key));
        }

    }

    public void insert(K key, V value) {
        if (key == null) {
            throw new RuntimeErrorException(new Error("Can't insert a null key!"));
        }
        if (value == null) {
            throw new RuntimeErrorException(new Error("Can't insert a key with null value!"));
        }
        if (root == null) {
            root = new BTreeNode<>(true);
        }
        IBTreeNode<K, V> checkDup = getNode(key);
        if (checkDup != null) {
            return;
        }
        if (root.getNumOfKeys() == 2 * t - 1) {
            IBTreeNode<K, V> newRoot = new BTreeNode<>(false);
            newRoot.getChildren().add(root);
            split(newRoot, 0);
            root = newRoot;
        }
        insertNotFull(key, value);
    }

    private void insertNotFull(K key, V value) {
        IBTreeNode<K, V> current = root;
        while (!current.isLeaf()) {
            int childIndex = getInsertionLoc(current.getKeys(), key);
            if (current.getChildren().get(childIndex).getNumOfKeys() < 2 * t - 1) {
                current = current.getChildren().get(childIndex);
            } else {
                if (key.compareTo(current.getChildren().get(childIndex).getKeys().get(t - 1)) < 0) {
                    split(current, childIndex);
                    current = current.getChildren().get(childIndex);

                } else {
                    split(current, childIndex);
                    current = current.getChildren().get(childIndex + 1);
                }
            }
        }
        int location = getInsertionLoc(current.getKeys(), key);
        current.getKeys().add(location, key);
        current.getValues().add(location, value);
    }

    public V search(K key) {
        if (key == null) {
            throw new RuntimeErrorException(new Error("Can't search for a null key!"));
        }
        if (root == null) {
            return null;
        }
        IBTreeNode<K, V> current = root;
        while (true) {
            for (int i = 0; i < current.getKeys().size(); i++) {
                if (current.getKeys().get(i).compareTo(key) == 0) {
                    return current.getValues().get(i);
                }
            }
            if (current.isLeaf()) {
                return null;
            }
            current = current.getChildren().get(getInsertionLoc(current.getKeys(), key));
        }

    }


    public boolean delete(K key) {
        if (key == null) {
            throw new RuntimeErrorException(new Error("Can't delete a null key!"));
        }
        if (root == null) {
            return false;
        }
        IBTreeNode<K, V> current = root;
        outer:
        while (true) {
            for (int i = 0; i < current.getKeys().size(); i++) {
                if (current.getKeys().get(i).compareTo(key) == 0) {
                    if (current.isLeaf()) {
                        current.getKeys().remove(i);
                        current.getValues().remove(i);
                        if (root.getNumOfKeys() == 0) {
                            root = null;
                        }
                        return true;
                    } else {
                        if (current.getChildren().get(i).getNumOfKeys() >= t) {
                            IBTreeNode<K, V> predecessor = predecessor(current, i);
                            current.getKeys().set(i, predecessor.getKeys().get(predecessor.getNumOfKeys() - 1));
                            current.getValues().set(i, predecessor.getValues().get(predecessor.getNumOfKeys() - 1));
                            current = current.getChildren().get(i);
                            key = predecessor.getKeys().get(predecessor.getNumOfKeys() - 1);
                        } else if (current.getChildren().get(i + 1).getNumOfKeys() >= t) {
                            IBTreeNode<K, V> successor = successor(current, i);
                            current.getKeys().set(i, successor.getKeys().get(0));
                            current.getValues().set(i, successor.getValues().get(0));
                            current = current.getChildren().get(i + 1);
                            key = successor.getKeys().get(0);
                        } else {
                            merge(current, i);
                            current = current.getChildren().get(i);
                        }
                        continue outer;
                    }
                }
            }
            if (current.isLeaf()) {
                return false;
            }
            int loc = getInsertionLoc(current.getKeys(), key);
            if (current.getChildren().get(loc).getNumOfKeys() < t) {
                if (loc != 0 && current.getChildren().get(loc - 1).getNumOfKeys() >= t) {
                    borrowFromPrev(current, loc);
                } else if (loc != current.getNumOfKeys() && current.getChildren().get(loc + 1).getNumOfKeys() >= t) {
                    borrowFromNext(current, loc);
                } else {
                    if (loc == current.getNumOfKeys()) {
                        loc--;
                    }
                    merge(current, loc);
                }
            }
            current = current.getChildren().get(loc);

        }
    }


    private IBTreeNode<K, V> successor(IBTreeNode<K, V> current, int keyIndex) {
        IBTreeNode<K, V> right = current.getChildren().get(keyIndex + 1);
        while (!right.isLeaf()) {
            right = right.getChildren().get(0);
        }
        return right;
    }

    private IBTreeNode<K, V> predecessor(IBTreeNode<K, V> current, int keyIndex) {
        IBTreeNode<K, V> left = current.getChildren().get(keyIndex);
        while (!left.isLeaf()) {
            left = left.getChildren().get(left.getChildren().size() - 1);
        }
        return left;
    }

    private void merge(IBTreeNode<K, V> parent, int childIndex) {
        if (parent == root && root.getNumOfKeys() == 1) {
            root = root.getChildren().get(childIndex);
        }
        parent.getChildren().get(childIndex).getKeys().add(parent.getKeys().remove(childIndex));
        parent.getChildren().get(childIndex).getValues().add(parent.getValues().remove(childIndex));
        parent.getChildren().get(childIndex).getKeys().addAll(parent.getChildren().get(childIndex + 1).getKeys());
        parent.getChildren().get(childIndex).getValues().addAll(parent.getChildren().get(childIndex + 1).getValues());
        if (!parent.getChildren().get(childIndex + 1).isLeaf()) {
            parent.getChildren().get(childIndex).getChildren().addAll(parent.getChildren().get(childIndex + 1).getChildren());
        }
        parent.getChildren().remove(childIndex + 1);
    }

    private void borrowFromPrev(IBTreeNode<K, V> parent, int childIndex) {
        parent.getChildren().get(childIndex).getKeys().add(0, parent.getKeys().get(childIndex - 1));
        parent.getChildren().get(childIndex).getValues().add(0, parent.getValues().get(childIndex - 1));
        parent.getKeys().set(childIndex - 1, parent.getChildren().get(childIndex - 1).getKeys().get(parent.getChildren().get(childIndex - 1).getKeys().size() - 1));
        parent.getValues().set(childIndex - 1, parent.getChildren().get(childIndex - 1).getValues().get(parent.getChildren().get(childIndex - 1).getValues().size() - 1));
        parent.getChildren().get(childIndex - 1).getKeys().remove(parent.getChildren().get(childIndex - 1).getKeys().size() - 1);
        parent.getChildren().get(childIndex - 1).getValues().remove(parent.getChildren().get(childIndex - 1).getValues().size() - 1);
        if (!parent.getChildren().get(childIndex).isLeaf()) {
            parent.getChildren().get(childIndex).getChildren().add(0, parent.getChildren().get(childIndex - 1).getChildren().remove(parent.getChildren().get(childIndex - 1).getChildren().size() - 1));
        }
    }

    private void borrowFromNext(IBTreeNode<K, V> parent, int childIndex) {
        parent.getChildren().get(childIndex).getKeys().add(parent.getKeys().get(childIndex));
        parent.getChildren().get(childIndex).getValues().add(parent.getValues().get(childIndex));
        parent.getKeys().set(childIndex, parent.getChildren().get(childIndex + 1).getKeys().get(0));
        parent.getValues().set(childIndex, parent.getChildren().get(childIndex + 1).getValues().get(0));
        parent.getChildren().get(childIndex + 1).getKeys().remove(0);
        parent.getChildren().get(childIndex + 1).getValues().remove(0);
        if (!parent.getChildren().get(childIndex).isLeaf()) {
            parent.getChildren().get(childIndex).getChildren().add(parent.getChildren().get(childIndex + 1).getChildren().remove(0));
        }
    }

    private int getInsertionLoc(List<K> keys, K key) {
        if (keys.isEmpty()) {
            return 0;
        }
        int lower = 0, higher = keys.size() - 1, mid, ceil = keys.size();
        while (lower <= higher) {
            mid = lower + (higher - lower) / 2;
            if (keys.get(mid).compareTo(key) >= 0) {
                ceil = mid;
                higher = mid - 1;
            } else {
                lower = mid + 1;
            }
        }
        return ceil;
    }

    private void split(IBTreeNode<K, V> current, int childIndex) {
        IBTreeNode<K, V> newNode;
        if (current.getChildren().get(childIndex).isLeaf()) {
            newNode = new BTreeNode<>(true);
        } else {
            newNode = new BTreeNode<>(false);
            newNode.getChildren().addAll(current.getChildren().get(childIndex).getChildren().subList(t, 2 * t));
            current.getChildren().get(childIndex).getChildren().subList(t, 2 * t).clear();
        }
        newNode.getKeys().addAll(current.getChildren().get(childIndex).getKeys().subList(t, 2 * t - 1));
        newNode.getValues().addAll(current.getChildren().get(childIndex).getValues().subList(t, 2 * t - 1));
        current.getChildren().get(childIndex).getKeys().subList(t, 2 * t - 1).clear();
        current.getChildren().get(childIndex).getValues().subList(t, 2 * t - 1).clear();
        int loc = getInsertionLoc(current.getKeys(), current.getChildren().get(childIndex).getKeys().get(t - 1));
        current.getKeys().add(loc, current.getChildren().get(childIndex).getKeys().get(t - 1));
        current.getValues().add(loc, current.getChildren().get(childIndex).getValues().get(t - 1));
        current.getChildren().get(childIndex).getKeys().remove(current.getChildren().get(childIndex).getKeys().size() - 1);
        current.getChildren().get(childIndex).getValues().remove(current.getChildren().get(childIndex).getValues().size() - 1);
        current.getChildren().add(loc + 1, newNode);
    }

}
