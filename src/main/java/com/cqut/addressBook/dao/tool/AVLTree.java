package com.cqut.addressBook.dao.tool;

import java.util.Comparator;
import java.util.Stack;
import java.util.function.Consumer;

/**
 * @author Augenstern
 * @date 2021/12/17
 */
public class AVLTree<T extends Comparable<T>> {
    private AVLNode<T> root;
    private int size;
    private Comparator<T> comparator;

    public AVLTree() {
    }

    public AVLTree(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    public int getSize() {
        return size;
    }

    private int compare(T o1, T o2) {
        if (this.comparator != null) {
            return comparator.compare(o1, o2);
        } else {
            return o1.compareTo(o2);
        }
    }

    public void add(T val) {
        AVLNode<T> node = new AVLNode<>();
        node.setVal(val);
        if (root == null) {
            this.root = node;
            this.size++;
            return;
        }
        this.add(this.root, node);
        balance(node);
    }

    private void add(AVLNode<T> root, AVLNode<T> node) {
        T val = node.getVal();
        AVLNode<T> temp = root;
        int i;
        do {
            i = this.compare(val, temp.getVal());
            if (i > 0) {
                if (temp.getRight() != null) {
                    temp = temp.getRight();
                } else {
                    node.setParent(temp);
                    temp.setRight(node);
                    break;
                }
            } else if (i < 0) {
                if (temp.getLeft() != null) {
                    temp = temp.getLeft();
                } else {
                    node.setParent(temp);
                    temp.setLeft(node);
                    break;
                }
            }
        } while (i != 0);
        if (i != 0) {
            this.size++;
        }
    }

    private void balance(AVLNode<T> node) {
        AVLNode<T> parent = node.getParent();
        if (parent != null) {
            boolean isLeftChild = parent.getLeft() != null;
            AVLNode<T> top = parent.getParent();
            if (top != null) {
                AVLNode<T> left = top.getLeft();
                AVLNode<T> right = top.getRight();
                int lh = left == null ? 0 : left.getHeight();
                int rh = right == null ? 0 : right.getHeight();
                int equilibrium = lh - rh;// <- 平衡因子
                if (equilibrium > 1) {
                    if (isLeftChild) {
                        //LL
                        this.llRotate(top);
                    } else {
                        //LR
                        this.lrRotate(top);
                    }
                } else if (equilibrium < -1) {
                    if (isLeftChild) {
                        //RL
                        this.rlRotate(top);
                    } else {
                        //RR
                        this.rrRotate(top);
                    }
                }
            }
        }

    }

    private void llRotate(AVLNode<T> node) {
        AVLNode<T> l = node.getLeft(), r = l.getRight(), p = node.getParent();
        l.setRight(node);
        node.setLeft(r);
        node.setParent(l);
        if (p == null) {
            this.root = l;
            l.setParent(null);
        } else {
            p.setLeft(l);
            l.setParent(p);
        }
    }

    private void lrRotate(AVLNode<T> node) {
        AVLNode<T> l = node.getLeft(), r = l.getRight();
        node.setLeft(r);
        r.setParent(node);
        r.setLeft(l);
        l.setParent(r);
        l.setRight(null);
        this.llRotate(l);
    }

    private void rrRotate(AVLNode<T> node) {
        AVLNode<T> r = node.getRight(), p = node.getParent();
        r.setLeft(node);
        node.setParent(r);
        node.setRight(null);
        if (p == null) {
            this.root = r;
            r.setParent(null);
        } else {
            p.setRight(r);
            r.setParent(p);
        }
    }

    private void rlRotate(AVLNode<T> node) {
        AVLNode<T> r = node.getRight(), l = r.getLeft();
        node.setRight(l);
        l.setParent(node);
        l.setRight(r);
        r.setLeft(null);
        this.rrRotate(node);
    }

    public void remove(T val) {
        AVLNode<T> node = this.search(val);
        if (node != null) {
            AVLNode<T> left = node.getLeft();
            AVLNode<T> right = node.getRight();
            if (left == null && right == null) {
                //Leaf node
                this.deleteLeafNode(node);
            } else if (left != null && right != null) {
                //Node with two children
                this.deleteNodeWithTwoChildren(node);
            } else {
                //Node with only one child
                this.deleteNodeWithOnlyChild(node);
            }
            this.size--;
        }
    }

    private void deleteLeafNode(AVLNode<T> node) {
        AVLNode<T> parent = node.getParent();
        if (parent != null) {
            if (this.compare(parent.getVal(), node.getVal()) > 0) {
                parent.setLeft(null);
            } else {
                parent.setRight(null);
            }
            node.setParent(null);
        } else {
            this.root = null;
        }

    }

    private void deleteNodeWithTwoChildren(AVLNode<T> node) {
        AVLNode<T> left = node.getLeft(), parent = node.getParent();
        AVLNode<T> precursorNode = null;
        if (left != null) {
            precursorNode = left;
            AVLNode<T> tempNode = precursorNode;
            do {
                tempNode = tempNode.getRight();
                if (tempNode != null) {
                    precursorNode = tempNode;
                }
            } while (tempNode != null);
        } else if (parent != null) {
            precursorNode = parent;
        }
        assert precursorNode != null;
        this.deleteLeafNode(precursorNode);
        node.setVal(precursorNode.getVal());
    }

    private void deleteNodeWithOnlyChild(AVLNode<T> node) {
        AVLNode<T> l = node.getLeft(), r = node.getRight();
        if (l != null) {
            node.setVal(l.getVal());
            this.deleteLeafNode(l);
        } else {
            node.setVal(r.getVal());
            this.deleteLeafNode(r);
        }
    }

    private AVLNode<T> search(T val) {
        AVLNode<T> node = this.root;
        while (node != null) {
            int i = this.compare(val, node.getVal());
            if (i > 0) {
                node = node.getRight();
            } else if (i == 0) {
                return node;
            } else {
                node = node.getLeft();
            }
        }
        return null;
    }

    public void forEach(Consumer<AVLNode<T>> handle) {
        Stack<AVLNode<T>> stack = new Stack<>();
        stack.push(this.root);
        while (!stack.isEmpty()) {
            AVLNode<T> top = stack.peek();
            AVLNode<T> left = top.getLeft();
            if (left != null) {
                stack.push(left);
            } else {
                while (!stack.isEmpty()) {
                    AVLNode<T> pop = stack.pop();
                    handle.accept(pop);
                    AVLNode<T> right = pop.getRight();
                    if (right != null) {
                        stack.push(right);
                        break;
                    }
                }
            }
        }
    }
}
