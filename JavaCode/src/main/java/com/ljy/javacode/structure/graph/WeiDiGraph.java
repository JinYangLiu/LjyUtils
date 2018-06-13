package com.ljy.javacode.structure.graph;

import java.util.Arrays;

/**
 * Created by LJY on 2018/6/12 21:35
 * 有权有向图
 * Weighted and Directed Graph
 * 重点是最短路径问题
 */
public class WeiDiGraph<T> {
    private final int MAX_VERTS = 20;
    private final int INFINITY = 1000*1000;//表示无穷大,不能到达
    private Vertex<T>[] vertexArray;
    private int adjMat[][];
    private int nVerts;
    private int nTree;
    private int currentVert;
    private int startToCurrent;
    private DistPar[] sPath;

    /**
     * 构造方法,初始化变量
     */
    public WeiDiGraph() {
        vertexArray = new Vertex[MAX_VERTS];
        adjMat = new int[MAX_VERTS][MAX_VERTS];
        nVerts = 0;
        nTree = 0;
        for (int i = 0; i < MAX_VERTS; i++)
            for (int j = 0; j < MAX_VERTS; j++)
                adjMat[i][j] = INFINITY;
        sPath = new DistPar[MAX_VERTS];
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
        //有向图的边是单向的,所以注释掉下面这一行
//        adjMat[end][start] = weight;
    }

    public void path() {
        int startTree = 0;
        vertexArray[startTree].isInTree = true;
        nTree = 1;
        for (int i = 0; i < nVerts; i++) {
            int tempDist = adjMat[startTree][i];
            sPath[i] = new DistPar(tempDist, startTree);
        }
        System.out.println(Arrays.asList(sPath).toString());
        while (nTree < nVerts) {
            int indexMin = getMin();
            System.out.println("indexMin:" + indexMin);
            int minDist = sPath[indexMin].distance;
            if (minDist == INFINITY) {
                System.out.println("There are unreachable vertices");
                break;
            } else {
                currentVert = indexMin;
                startToCurrent = sPath[indexMin].distance;
            }
            vertexArray[currentVert].isInTree = true;
            nTree++;
            adjust_sPath();
            System.out.println(Arrays.asList(sPath).toString());
        }//end while

        displayPaths();

        nTree = 0;
        for (int i = 0; i < nVerts; i++) {
            vertexArray[i].isInTree = false;
        }
    }

    private void displayPaths() {
        System.out.println(Arrays.asList(sPath).toString());
        for (int i = 0; i < nVerts; i++) {
            System.out.print(vertexArray[i].value + "=");
            if (sPath[i].distance == INFINITY)
                System.out.print("inf");//不可达
            else
                System.out.print(sPath[i].distance);
            T parent = vertexArray[sPath[i].parentVertex].value;
            System.out.print("(" + parent + ")");

        }//end for i
        System.out.println();
    }

    private void adjust_sPath() {
        int column = 1;
        while (column < nVerts) {
            if (vertexArray[column].isInTree) {
                column++;
                continue;
            }
            int currentToFringe = adjMat[currentVert][column];
            int startToFringe = startToCurrent + currentToFringe;
            int sPthDist = sPath[column].distance;
            System.out.println("startToCurrent:"+startToCurrent+",currentToFringe:" + currentToFringe+",startToFringe:" + startToFringe + ",sPthDist:" + sPthDist);
            if (startToFringe < sPthDist) {
                sPath[column].parentVertex = currentVert;
                sPath[column].distance = startToFringe;
            }
            column++;
        }//end while
    }

    private int getMin() {
        int minDist = INFINITY;
        int indexMin = 0;
        for (int i = 1; i < nVerts; i++) {
            if (!vertexArray[i].isInTree && sPath[i].distance < minDist) {
                minDist = sPath[i].distance;
                indexMin = i;
            }
        }
        return indexMin;
    }
}
