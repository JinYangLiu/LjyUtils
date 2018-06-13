package com.ljy.javacode.structure.graph;

/**
 * Created by LJY on 2018/6/12 14:06
 * 有向图的实现类
 * Directed  Graph
 * 重点是 拓扑排序 和 连通性
 */
public class DiGraph<T> {
    private final int MAX_VERTS = 20;
    private Vertex<T>[] vertexArray;
    private int adjMat[][];
    private int nVerts;
    private T[] sortArray;

    public DiGraph() {
        nVerts = 0;
        vertexArray = new Vertex[MAX_VERTS];
        adjMat = new int[MAX_VERTS][MAX_VERTS];
        for (int i = 0; i < MAX_VERTS; i++) {
            for (int j = 0; j < MAX_VERTS; j++) {
                adjMat[i][j] = 0;
            }
        }
        sortArray=(T[])new Object[MAX_VERTS];
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
        //有向图与无向图的主要区别在代码中的体现就是这一步
//        adjMat[end][start] = 1;//有向图不需要这一行
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
     * 拓扑排序
     * topological sort
     */
    public void topoSort(){
        int orig_nVerts=nVerts;
        while (nVerts>0){
            int currentVertex=noSuccessors();
            if (currentVertex==-1){
                System.out.println("Error: Graph has cycles");
                return;
            }
            sortArray[nVerts-1]=vertexArray[currentVertex].value;
            deleteVertex(currentVertex);
        }
        System.out.print("topological sort: ");
        for (int i = 0; i < orig_nVerts; i++)
            System.out.print(sortArray[i]);
        System.out.println();
    }

    private void deleteVertex(int delVertex) {
        if (delVertex!=nVerts-1){
            for (int i = delVertex; i <nVerts-1 ; i++)
                vertexArray[i]=vertexArray[i+1];
            for (int row = delVertex; row < nVerts-1; row++)
                moveRowUp(row,nVerts);
            for (int col = delVertex; col <nVerts-1 ; col++)
                moveColLeft(col,nVerts-1);
        }
        nVerts--;
    }

    private void moveRowUp(int row, int length) {
        for (int col = 0; col < length; col++)
            adjMat[row][col]=adjMat[row+1][col];
    }

    private void moveColLeft(int col, int length) {
        for (int row = 0; row <length ; row++)
            adjMat[row][col]=adjMat[row][col+1];
    }

    private int noSuccessors() {
        boolean isEdge;
        for (int row = 0; row < nVerts; row++) {
            isEdge=false;
            for (int col = 0; col < nVerts; col++) {
                if (adjMat[row][col]>0) {
                    isEdge = true;
                    break;
                }
            }
            if (!isEdge)
                return row;
        }
        return -1;
    }


    /**
     * Warshall算法利用图的邻接矩阵求出原图的传递闭包
     *
     *  其实下面代码的意思就是,如果有i-->j, 在找到k-->i, 那么k-->j
     *  即若B连通C,A连通B,那么A连通C
     */
    public void warshall(){
        for (int i = 0; i < MAX_VERTS; i++) {//行
            for (int j = 0; j < MAX_VERTS; j++) {//列
                if (adjMat[i][j]==1){// i-->j
                    for (int k = 0; k < MAX_VERTS; k++) { // k-->i , 找到连接i的顶点
                        if (adjMat[k][i]==1&&k!=j){
                            adjMat[k][j]=1;//k-->j
                        }
                    }
                }
            }
        }
    }

    public void display(){
        for (int i = 0; i < MAX_VERTS; i++) {
            for (int j = 0; j < MAX_VERTS; j++) {
                System.out.print(adjMat[i][j]+" ");
            }
            System.out.println();
        }
    }
}
