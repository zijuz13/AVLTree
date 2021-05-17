package dsa.example;
import dsa.impl.AVLTree;

import java.util.HashMap;

public class AVLTreeTestDemo {
    public static void main(String[] args) {
        AVLTree<Integer> avlTree=new AVLTree<>();
        for(int i=0;i<100;++i){
            avlTree.insert(i);
        }
        System.out.println(avlTree.contains(8));
        System.out.println(avlTree.isBalanced());
    }
}
