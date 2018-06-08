package com.ljy.javacode.structure.tree;

/**
 * Created by LJY on 2018/6/7 21:17
 *
 * 2-3-4树的节点类
 */
public class Node234<K extends Comparable<K>,T>  {
    private static final int ORDER = 4;
    private int numItems;//数据项个数
    private Node234<K, T> parent;
    private DataItem<K,T>[] itemArray;//子节点数组
    private Node234<K, T>[] childArray ;//数据项数组

    public Node234() {
        itemArray=new DataItem[ORDER-1];
        childArray = new Node234[ORDER];
    }

    //connect child to this node
    public void connectChild(int childNum, Node234<K, T> child) {
        childArray[childNum] = child;
        if (child != null)
            child.parent = this;
    }

    //disconnect child from this node,return it
    public Node234<K, T> disconnectChild(int childNum) {
        Node234<K, T> temp = childArray[childNum];
        childArray[childNum] = null;
        return temp;
    }

    public Node234<K,T> getChild(int childNum){
        return childArray[childNum];
    }

    public Node234<K,T> getParent(){
        return parent;
    }

    //是否叶子节点
    public boolean isLeaf(){
        return childArray[0] == null;
    }

    public int getNumItems(){
        return numItems;
    }

    public DataItem<K,T> getItem(int index){
        return itemArray[index];
    }

    public boolean isFull(){
        return numItems==ORDER-1;
    }



   public int findItem(K key){
       for (int i = 0; i < ORDER - 1; i++) {
           if (itemArray[i]==null)
               break;
           else if (itemArray[i].key.compareTo(key)==0)
               return i;
       }
       return -1;
   }

   public int insertItem(DataItem<K,T> newItem){
        numItems++;
        K newKey=newItem.key;
       for (int i = ORDER-2; i >=0 ; i--) {
           if (itemArray[i]==null)
               continue;
           else {
               K itemKey=itemArray[i].key;
               int cmp=newKey.compareTo(itemKey);
               if (cmp<0){
                   itemArray[i+1]=itemArray[i];
               }else {
                   itemArray[i+1]=newItem;
                   return i+1;
               }
           }
       }
       itemArray[0]=newItem;
       return 0;
   }

   public DataItem<K,T> deleteItem(){
       DataItem<K,T> temp=itemArray[numItems-1];
       itemArray[numItems-1]=null;
       numItems--;
       return temp;
   }

   public void display(){
       System.out.print(" Node234:{ ");
       for (int i = 0; i < numItems; i++) {
           itemArray[i].display();
           if (i!=numItems-1)
               System.out.print(",");
       }
       System.out.println("} ");
   }


    /**
     * 数据项的模型类
     */
    public static class DataItem<K,T>{
        public K key;
        public T data;

        public void display(){
            System.out.print(" DataItem:{ key = "+key.toString()+", data = "+data.toString()+"}");
        }
    }
}
