package com.cqut.addressBook.dao.tool;

/**
 * @author Augenstern
 * @date 2021/12/17
 */
public class AVLNode<T extends Comparable<T>> {
    private T val;
    private AVLNode<T> parent;
    private AVLNode<T> left;
    private AVLNode<T> right;

    public AVLNode() {
    }

    public T getVal() {
        return val;
    }

    public void setVal(T val) {
        this.val = val;
    }

    public AVLNode<T> getParent() {
        return parent;
    }

    public void setParent(AVLNode<T> parent) {
        this.parent = parent;
    }

    public AVLNode<T> getLeft() {
        return left;
    }

    public void setLeft(AVLNode<T> left) {
        this.left = left;
    }

    public AVLNode<T> getRight() {
        return right;
    }

    public void setRight(AVLNode<T> right) {
        this.right = right;
    }

    public int getHeight() {
        int lh = this.left == null ? 0 : this.left.getHeight(), rh = this.right == null ? 0 : this.right.getHeight();
        return Math.max(lh, rh) + 1;
    }
}
