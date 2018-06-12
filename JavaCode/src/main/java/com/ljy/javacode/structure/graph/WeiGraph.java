package com.ljy.javacode.structure.graph;

import com.ljy.javacode.structure.queue.PriorityArrayQueue;

/**
 * Created by LJY on 2018/6/12 16:28
 * 无向带权图
 * Weighted Graph
 */
public class WeiGraph<T> {
    private final int MAX_VERTS = 20;
    private final int INFINITY = 1000 * 1000;
    private Vertex<T>[] vertexArray;
    private int adjMat[][];
    private int nVerts;
    private int currentVert;
    private MyPQ queue;
    private int nTree;

    /**
     * 初始化数据
     */
    public WeiGraph() {
        vertexArray = new Vertex[MAX_VERTS];
        adjMat = new int[MAX_VERTS][MAX_VERTS];
        nVerts = 0;
        for (int i = 0; i < MAX_VERTS; i++)
            for (int j = 0; j < MAX_VERTS; j++)
                adjMat[i][j] = INFINITY;
        queue = new MyPQ(20);
    }

    /**
     * 添加一个顶点
     */
    public void addVertex(T value) {
        vertexArray[nVerts++] = new Vertex<>(value);
    }

    /**
     * 添加边
     */
    public void addEdge(int start, int end, int weight) {
        adjMat[start][end] = weight;
        adjMat[end][start] = weight;
    }

    /**
     * 展示指定顶点
     *
     * @param index
     */
    public void displayVertex(int index) {
        System.out.print(vertexArray[index].value);
    }

    /**
     * 计算无向带权图的最小生成树
     * Minimum Spanning Tree Weighted
     */
    public void mstw() {
        currentVert = 0;
        while (nTree < nVerts - 1) {
            vertexArray[currentVert].isInTree = true;
            nTree++;
            for (int i = 0; i < nVerts; i++) {
                if (i == currentVert)
                    continue;
                if (vertexArray[i].isInTree)
                    continue;
                int distance = adjMat[currentVert][i];
                if (distance == INFINITY)
                    continue;
                putInPQ(i, distance);
            }//end for i
            if (queue.size() == 0) {
                System.out.println("Graph not connected");
                return;
            }
            //remove from queue
            Edge theEdge = queue.removeMin();
            int startVert = theEdge.startVert;
            currentVert = theEdge.endVert;
            //display
            System.out.print(vertexArray[startVert].value);
            System.out.print(vertexArray[currentVert].value);
            System.out.print("  ");
        }//end while

        //mst is complete
        for (int i = 0; i < nVerts; i++)
            vertexArray[i].isInTree = false;
    }

    //将顶点存入优先级队列
    private void putInPQ(int newVert, int newDist) {
        int queueIndex = queue.find(newVert);
        if (queueIndex != -1) {
            Edge tempEdge = queue.peek(queueIndex);
            int oldDist = tempEdge.distance;
            if (oldDist > newDist) {
                queue.remove(queueIndex);
                Edge theEdge = new Edge(currentVert, newVert, newDist);
                queue.insert(theEdge);
            }
        } else {
            Edge theEdge = new Edge(currentVert, newVert, newDist);
            queue.insert(theEdge);
        }
    }

    public static class MyPQ extends PriorityArrayQueue<Edge> {

        public MyPQ(int maxSize) {
            super(maxSize, new Edge[maxSize]);
        }

        //重写
        @Override
        public void insert(Edge value) {
            int j;
            for (j = 0; j < size; j++)
                if (value.distance >= queArray[j].distance)
                    break;
            for (int i = size - 1; i >= j; i--)
                queArray[i + 1] = queArray[i];
            queArray[j]=value;
            size++;
        }

        //重载
        public int find(int findDex) {
            for (int i = 0; i < size; i++)
                if (queArray[i].endVert == findDex)
                    return i;
            return -1;
        }
    }

}
