package dsa.impl;

import dsa.iface.IIterator;
import dsa.iface.IPosition;

import java.util.Deque;
import java.util.LinkedList;

public class AVLTree<T extends Comparable<T>> extends BinarySearchTree<T> {
   public AVLTree(){
      super();
   }
   @Override
   public void insert( T element ) {
      // TODO: Implement the insert(...) method.
      this.insertRecursively(this.root(),element);
   }
   //Use Recursion to insert the IPosition nodes and check if the subTree is balanced every time we successfully balance the
   //previous nodes and modify the heights simultaneously
   private void insertRecursively(IPosition<T> position,T element){
      if(isExternal(position)){
         this.expandExternal(position,element);
      }else{
         int minus=position.element().compareTo(element);
         if(minus>=0){
            this.insertRecursively(this.left(position),element);
         }else{
            this.insertRecursively(this.right(position),element);
         }
      }
      this.restructure(position);
   }
   @Override
   public boolean contains( T element ) {
      // TODO: Implement the contains(...) method.
      IPosition<T> tiPosition = this.find(this.root, element);
      return !this.isExternal(tiPosition); // this line is here just so that the class will compile. You should replace it.
   }

   @Override
   public void remove( T element ) {
      // TODO: Implement the remove(...) method.
     this.removeRecursively(this.root(),element);
   }
   private void removeRecursively(IPosition<T> position,T element){
      AVLPosition position1 = (AVLPosition) position;
      //No element is found
      if(this.isExternal(position1))return;
      int minus=position1.element().compareTo(element);
      if(0==minus){
         this.remove(position1);
      }else if(minus>0){
         this.removeRecursively(this.left(position1),element);
      }else{
         this.removeRecursively(this.right(position1),element);
      }
      this.restructure(position1);
   }
   private void restructure( IPosition<T> x ) {
      // Implement the restructure(...) method.
      AVLPosition x1 = (AVLPosition) x;
      int leftHeight=this.height(x1.left);
      int rightHeight=this.height(x1.right);
      boolean isRotated=false;
      //Only after rotating this avl tree should we recalculate the heights of the nodes
      if(leftHeight-rightHeight>1){
         isRotated=true;
         if(this.height(x1.left.left)<this.height(x1.left.right)){
            leftRotate(x1);
         }
         rightRotate(x1);
      }else if(rightHeight-leftHeight>1){
         isRotated=true;
         if(this.height(x1.right.right)<this.height(x1.right.left)){
            rightRotate(x1);
         }
         leftRotate(x1);
      }
      if(isRotated){
         this.calculateHeights(x1);
      }else{
         x1.height=Math.max(leftHeight,rightHeight)+1;
      }
   }
   //Inorder Traversal
   public void calculateHeights(BTPosition position){
      Deque<BTPosition> stack=new LinkedList<>();
      BTPosition node=position;
      while(this.isInternal(node)||!stack.isEmpty()){
         while(this.isInternal(node)){
            stack.push(node);
            node=node.left;
         }
         node=stack.pop();
         ((AVLPosition)node).height=this.height(node);
         node=node.right;
      }
   }
   private void leftRotate(BTPosition position){
      BTPosition newNode = this.newPosition(position.element(), position);
     newNode.left=newPosition(null,null);
     newNode.right=newPosition(null,null);
      if(null!=position.left)
         position.left.parent=newNode;
      newNode.left=position.left;
      if(null!=position.right.left)
         position.right.left.parent=newNode;
      newNode.right=position.right.left;
      position.element=position.right.element;
      if(null!=position.right.right)
         position.right.right.parent=position;
      position.right=position.right.right;
      position.left=newNode;
   }
   private void rightRotate(BTPosition position){
      BTPosition newNode = this.newPosition(position.element, position);
      newNode.left=newPosition(null,null);
      newNode.right=newPosition(null,null);
      if(null!=position.right)
         position.right.parent=newNode;
      newNode.right=position.right;
      if(null!=position.left.right)
         position.left.right.parent=newNode;
      newNode.left=position.left.right;
      position.element=position.left.element;
      position.right=newNode;
      if(null!=position.left.left)
         position.left.left.parent=position;
      position.left=position.left.left;
   }
   //To check if this AVLTree has been balance
   public boolean isBalanced(){
      return this.isBalanced(this.root);
   }
   public boolean isBalanced(BTPosition position){
      if(this.isExternal(position))return true;
      return isBalanced(position.left)&&judgeIfBalanced(position)&&isBalanced(position.right);
   }
   private boolean judgeIfBalanced(BTPosition position){
      return Math.abs(this.height(position.left)-this.height(position.right))<=1;
   }
   //Calculate the height of a single node
   private int height(BTPosition position){
      if(null==position||this.isExternal(position))return 0;
      return Math.max(this.height(position.left),this.height(position.right))+1;
   }
   private int height1(BTPosition position){
      return ((AVLPosition)position).height;
   }
   @Override
   protected BTPosition newPosition( T element, BTPosition parent ) {
      return new AVLPosition( element, parent );
   }

   public class AVLPosition extends BTPosition {
      public int height = 0;

      public AVLPosition( T element, BTPosition parent ) {
         super( element, parent );
      }
   }
}
