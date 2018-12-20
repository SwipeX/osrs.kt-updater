package org.objectweb.asm.commons.cfg.graph;

import java.util.*;

/**
 * @author Tyler Sedlar
 */
public class Digraph<V, E> implements Iterable<V> {

    private final Map<V, Set<E>> graph = new HashMap<>();
    private CyclomaticComplexity cyclomatic = null;

    @SuppressWarnings("unchecked")
    public Set<E> edgeAt(int index) {
        return (Set<E>) graph.values().toArray()[index];
    }

    public int size() {
        return graph.size();
    }

    public boolean containsVertex(V vertex) {
        return graph.containsKey(vertex);
    }

    public boolean containsEdge(V vertex, E edge) {
        return graph.containsKey(vertex) && graph.get(vertex).contains(edge);
    }

    public boolean addVertex(V vertex) {
        if (graph.containsKey(vertex)) {
            return false;
        }
        graph.put(vertex, new HashSet<E>());
        return true;
    }

    public void addEdge(V start, E dest) {
        if (!graph.containsKey(start)) {
            return;
        }
        graph.get(start).add(dest);
    }

    public void removeEdge(V start, E dest) {
        if (!graph.containsKey(start)) {
            return;
        }
        graph.get(start).remove(dest);
    }

    public Set<E> edgesFrom(V node) {
        return Collections.unmodifiableSet(graph.get(node));
    }

    public void graph(Digraph<V, E> graph) {
        this.graph.putAll(graph.graph);
    }

    @Override
    public final Iterator<V> iterator() {
        return graph.keySet().iterator();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (V v : graph.keySet()) {
            builder.append("\n    ").append(v).append(" -> ").append(graph.get(v));
        }
        return builder.toString();
    }

    public void flush() {
        graph.clear();
    }

    /**
     * Gets the complexity for this graph.
     *
     * @param cached <t>true</t> to get the cached complexity.
     * @return the complexity for this graph.
     */
    public CyclomaticComplexity cyclomatic(boolean cached) {
        if (cached && cyclomatic != null) {
            return cyclomatic;
        }
        int edges = 0;
        for (Set<E> set : graph.values()) {
            edges += set.size();
        }
        int connections = 0;
        for (Set<E> set : graph.values()) {
            if (!set.isEmpty()) {
                connections++;
            }
        }
        return (cyclomatic = new CyclomaticComplexity(edges, size(), connections));
    }

    /**
     * Gets the complexity for this graph.
     *
     * @return the complexity for this graph.
     */
    public CyclomaticComplexity cyclomatic() {
        return cyclomatic(true);
    }
}