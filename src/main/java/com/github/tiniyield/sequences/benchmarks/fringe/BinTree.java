package com.github.tiniyield.sequences.benchmarks.fringe;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.stream.Stream;

public class BinTree<T extends Comparable<T>> implements Iterable<T> {
    public BinTree<T> left;
    public BinTree<T> right;
    public T val;

    public BinTree(T val, BinTree<T> left, BinTree<T> right) {
        this.left = left;
        this.right = right;
        this.val = val;
    }

    public BinTree(T val) {
        this(val, null, null);
    }

    public BinTree(Stream<T> vals) {
        Iterator<T> iter = vals.iterator();
        val = iter.next();
        while(iter.hasNext()) {
            insert(iter.next());
        }
    }

    public boolean isLeaf() {
        return ((left == null) && (right == null));
    }

    public void insert(T value) {
        int cmp = value.compareTo(this.val);
        if (cmp == 0)
            throw new IllegalArgumentException("Same data!");
        if (cmp < 0) {
            if (left == null)
                left = new BinTree<>(value);
            else
                left.insert(value);
        } else {
            if (right == null)
                right = new BinTree<>(value);
            else
                right.insert(value);
        }
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new BinTreeIterator<>(this);
    }

    static class BinTreeIterator<U extends Comparable<U>> implements Iterator<U> {
        private final Stack<BinTree<U>> stack;
        private BinTree<U> current;
        public BinTreeIterator(BinTree<U> src) {
            stack = new Stack<>();
            stack.push(src);
        }

        private BinTree<U> advanceToLeaf() {
            while(!stack.isEmpty()) {
                BinTree<U> node = stack.pop();
                if(node.isLeaf()){
                    return node;
                }
                if(node.right != null) {
                    stack.push(node.right);
                }
                if(node.left != null) {
                    stack.push(node.left);
                }
            }
            return null;
        }



        @Override
        public boolean hasNext() {
            return current != null || (current = advanceToLeaf()) != null;
        }

        @Override
        public U next() {
            if((current == null || current.val == null)){
                current = advanceToLeaf();
                if(current == null){
                    throw new NoSuchElementException("");
                }
            }
            U aux = current.val;
            current = null;
            return aux;
        }
    }
}
