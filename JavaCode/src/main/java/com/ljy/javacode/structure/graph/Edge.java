package com.ljy.javacode.structure.graph;

import android.support.annotation.NonNull;

/**
 * Created by LJY on 2018/6/12 16:22
 * 带权图的边
 */
public class Edge implements Comparable<Edge> {
    public int startVert;//边的起始/出发顶点
    public int endVert;//边的结束/目的顶点
    public int distance;//权值

    public Edge(int startVert, int endVert, int distance) {
        this.startVert = startVert;
        this.endVert = endVert;
        this.distance = distance;
    }


    @Override
    public int compareTo(@NonNull Edge other) {
        return distance-other.distance;
    }
}
