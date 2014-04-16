package com.subway.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.graph.GraphPathImpl;
import org.jgrapht.traverse.ClosestFirstIterator;

import com.subway.model.Station.Shape_type;

public final class ShortestPath<V extends Station , E>
{
    

    private GraphPath<V, E> path;
    private V endStation;
    

    /**
     * Creates and executes a new DijkstraShortestPath algorithm instance. An
     * instance is only good for a single search; after construction, it can be
     * accessed to retrieve information about the path found.
     *
     * @param graph the graph to be searched
     * @param startVertex the vertex at which the path should start
     * @param endVertex the vertex at which the path should end
     */
    public ShortestPath(Graph<V, E> graph,
        V startVertex,
        Shape_type endType)
    {
        this(graph, startVertex, endType, Double.POSITIVE_INFINITY);
    }

    /**
     * Creates and executes a new DijkstraShortestPath algorithm instance. An
     * instance is only good for a single search; after construction, it can be
     * accessed to retrieve information about the path found.
     *
     * @param graph the graph to be searched
     * @param startVertex the vertex at which the path should start
     * @param endVertex the vertex at which the path should end
     * @param radius limit on path length, or Double.POSITIVE_INFINITY for
     * unbounded search
     */
    public ShortestPath(
        Graph<V, E> graph,
        V startVertex,
        Shape_type endType,
        double radius)
    {

        ClosestFirstIterator<V, E> iter =
            new ClosestFirstIterator<V, E>(graph, startVertex, radius);

        while (iter.hasNext()) {
            V vertex = iter.next();
            
            if (vertex.getType()==endType) {
            	endStation = vertex;
                createEdgeList(graph, iter, startVertex, vertex);
                return;
            }
        }

        path = null;
    }

    public V getEndStation(){
    	return endStation;
    }

    /**
     * Return the edges making up the path found.
     *
     * @return List of Edges, or null if no path exists
     */
    public List<E> getPathEdgeList()
    {
        if (path == null) {
            return null;
        } else {
            return path.getEdgeList();
        }
    }

    /**
     * Return the path found.
     *
     * @return path representation, or null if no path exists
     */
    public GraphPath<V, E> getPath()
    {
        return path;
    }

    /**
     * Return the length of the path found.
     *
     * @return path length, or Double.POSITIVE_INFINITY if no path exists
     */
    public double getPathLength()
    {
        if (path == null) {
            return Double.POSITIVE_INFINITY;
        } else {
            return path.getWeight();
        }
    }

    /**
     * Convenience method to find the shortest path via a single static method
     * call. If you need a more advanced search (e.g. limited by radius, or
     * computation of the path length), use the constructor instead.
     *
     * @param graph the graph to be searched
     * @param startVertex the vertex at which the path should start
     * @param endVertex the vertex at which the path should end
     *
     * @return List of Edges, or null if no path exists
     */
    public static <V extends Station, E> List<E> findPathBetween(
        Graph<V, E> graph,
        V startVertex,
        Shape_type endVertex)
    {
        ShortestPath<V, E> alg =
            new ShortestPath<V, E>(
                graph,
                startVertex,
                endVertex);

        return alg.getPathEdgeList();
    }

    private void createEdgeList(
        Graph<V, E> graph,
        ClosestFirstIterator<V, E> iter,
        V startVertex,
        V endVertex)
    {
        List<E> edgeList = new ArrayList<E>();

        V v = endVertex;

        while (true) {
            E edge = iter.getSpanningTreeEdge(v);

            if (edge == null) {
                break;
            }

            edgeList.add(edge);
            v = Graphs.getOppositeVertex(graph, edge, v);
        }

        Collections.reverse(edgeList);
        double pathLength = iter.getShortestPathLength(endVertex);
        path =
            new GraphPathImpl<V, E>(
                graph,
                startVertex,
                endVertex,
                edgeList,
                pathLength);
    }
}