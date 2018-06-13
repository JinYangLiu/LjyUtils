package com.ljy.javacode.structure.graph;

/**
 * Created by LJY on 2018/6/12 21:30
 * 有权有向图最短路径问题的一个辅助类
 * 由于记住路径(权值的和)和父节点(即 distance & parentVertex,故而命名为DistPar)
 */
public class DistPar {
    public int distance;
    public int parentVertex;

    public DistPar(int distance, int parentVertex) {
        this.distance = distance;
        this.parentVertex = parentVertex;
    }

    @Override
    public String toString() {
        return "DistPar{" +
                "distance=" + distance +
                ", parentVertex=" + parentVertex +
                '}';
    }
}
