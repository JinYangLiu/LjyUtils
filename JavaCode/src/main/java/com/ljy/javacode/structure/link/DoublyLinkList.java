package com.ljy.javacode.structure.link;

/**
 * Created by LJY on 2018/6/5 10:27
 *
 * 双向链表:
 *
 * 传统链表存在的问题: 沿链表反向遍历比较困难,很难取得前一个链结点
 *
 *  关键点: 每个链结点有两个指向其他链结点的引用,而不是一个
 *
 *  缺点: 每次插入或删除一个链结点时,要处理四个链结点的应用,而不是两个
 */
public class DoublyLinkList<T>{
    private DoublyLink<T> first;
    private DoublyLink<T> last;

    public boolean isEmpty(){
        return first==null;
    }

    public void insertFirst(T value){
        DoublyLink<T> newLink=new DoublyLink<>(value);
        if (isEmpty())
            last=newLink;
        else
            first.left=newLink;
        newLink.right=first;
        first=newLink;
    }

    public void insterLast(T value){
        DoublyLink<T> newLink=new DoublyLink<>(value);
        if (isEmpty())
            first=newLink;
        else {
            last.right=newLink;
            newLink.left=last;
        }
        last=newLink;
    }

    /**
     * 在key后插入value
     * @param key
     * @param value
     * @return
     */
    public boolean insertAfter(T key,T value){
        DoublyLink<T> current=first;
        while (current.data!=key){
            current=current.right;
            if (current==null){
                return false;
            }
        }
        DoublyLink<T> newLink=new DoublyLink<>(value);
        if (current==last){
            newLink.right=null;
            last=newLink;
        }else {
            newLink.right=current.right;
            current.right.left=newLink;
        }
        newLink.left=current;
        current.right=newLink;
        return true;
    }

    public DoublyLink<T> deleteFirst(){
        DoublyLink temp=first;
        if (first.right==null)
            last=null;
        else
            first.right.left=null;
        first=first.right;
        return temp;
    }

    public DoublyLink<T> deleteLast(){
        DoublyLink temp=last;
        if (first.right==null)
            first=null;
        else
            last.left.right=null;
        last=last.left;
        return temp;
    }

    public DoublyLink<T> delete(T value){
        DoublyLink<T> current=first;
        while (current.data!=value) {
            current = current.right;
            if (current==null)
                return null;
        }

        if (current==first)
            first=current.right;
        else
            current.left.right=current.right;

        if (current==last)
            last=current.left;
        else
            current.right.left=current.left;

        return current;
    }

    public void displayForward(){
        System.out.print("DoublyLinkList(first-->last): ");
        DoublyLink<T> current=first;
        while (current!=null){
            current.display();
            current=current.right;
        }
        System.out.println();
    }

    public void displayBackground(){
        System.out.print("DoublyLinkList(last-->first): ");
        DoublyLink<T> current=last;
        while (current!=null){
            current.display();
            current=current.left;
        }
        System.out.println();
    }
}
