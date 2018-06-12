package com.ljy.javacode.structure.graph;

import com.ljy.javacode.structure.queue.ArrayQueue;
import com.ljy.javacode.structure.queue.Queue;
import com.ljy.javacode.structure.stack.ArrayStack;

/**
 * Created by LJY on 2018/6/11 17:29
 * 图的实现
 */
public class Graph<T> {
    private final int MAX_VERTS = 20;
    private Vertex<T>[] vertexArray;
    private int adjMat[][];//邻接矩阵
    private int nVerts;
    private ArrayStack<Integer> stack;
    private ArrayQueue<Integer> queue;

    /**
     * 构造方法,初始化数据
     */
    public Graph() {
        nVerts = 0;
        vertexArray = new Vertex[MAX_VERTS];
        adjMat = new int[MAX_VERTS][MAX_VERTS];
        for (int i = 0; i < MAX_VERTS; i++) {
            for (int j = 0; j < MAX_VERTS; j++) {
                adjMat[i][j] = 0;
            }
        }
        stack = new ArrayStack<>(MAX_VERTS);
        queue = new ArrayQueue<>(MAX_VERTS);
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
    public void addEdge(int start, int end) {
        adjMat[start][end] = 1;
        adjMat[end][start] = 1;
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
     * 使用深度优先搜索展示图
     */
    public void displayDFS() {
        vertexArray[0].wasVisited = true;
        displayVertex(0);
        stack.push(0);
        while (!stack.isEmpty()) {
            int v = getAdjUnvisitedVertex(stack.peek());
            if (v == -1) {
                System.out.println(" 没有未访问的邻接顶点了,返回上一个顶点看看吧");
                stack.pop();//规则2:没有未访问的邻接顶点时,若栈不为空,就从栈中弹出一个顶点
                if (!stack.isEmpty()) {
                    displayVertex(stack.peek());
                }
            } else {
                vertexArray[v].wasVisited = true;
                System.out.print("-->");
                displayVertex(v);
                stack.push(v);
            }
        }
        initWasVisited();
    }

    /**
     * 使用广度优先搜索展示图
     */
    public void displayBFS() {
        vertexArray[0].wasVisited = true;
        displayVertex(0);
        queue.insert(0);
        int v2;
        while (!queue.isEmpty()) {
            int v1 = queue.remove();
            while ((v2 = getAdjUnvisitedVertex(v1)) != -1) {
                vertexArray[v2].wasVisited = true;
                displayVertex(v2);
                queue.insert(v2);
            }
        }
        initWasVisited();
    }

    /**
     * 实现最小生成树算法,排除多余的边,找到连接所有顶点的最少的边
     * 使用DFS实现比较容易,因为DFS访问所有顶点,但只访问一次
     */
    public void mst() {
        vertexArray[0].wasVisited = true;
        stack.push(0);
        while (!stack.isEmpty()) {
            int currentVertex = stack.peek();
            int v = getAdjUnvisitedVertex(currentVertex);
            if (v == -1)
                stack.pop();
            else {
                vertexArray[v].wasVisited = true;
                stack.push(v);

                displayVertex(currentVertex);
                displayVertex(v);
                System.out.print("  ");
            }
        }
        initWasVisited();
    }

    private void initWasVisited() {
        for (int i = 0; i < nVerts; i++)
            vertexArray[i].wasVisited = false;
    }

    //规则1:寻找传入顶点v的一个未访问的邻接顶点
    private int getAdjUnvisitedVertex(int v) {
        for (int i = 0; i < nVerts; i++)
            if (adjMat[v][i] == 1 && !vertexArray[i].wasVisited)
                return i;
        return -1;
    }


}
