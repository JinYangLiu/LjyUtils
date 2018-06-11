package com.ljy.javacode.structure.graph;

/**
 * Created by LJY on 2018/6/11 17:29
 * 图的实现
 */
public class Graph<T> {
    private final int MAX_VERTS = 20;
    private Vertex<T>[] vertexArray;
    private int adjMat[][];//邻接矩阵
    private int nVerts;

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

    public void displayVertex(int index){
        System.out.print(vertexArray[index].value);
    }


}
