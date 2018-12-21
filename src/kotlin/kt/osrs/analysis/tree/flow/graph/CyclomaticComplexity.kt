/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package kt.osrs.analysis.tree.flow.graph

/**
 * @author Tyler Sedlar
 */
class CyclomaticComplexity(private val edges: Int, private val nodes: Int, private val connections: Int) {

    private var type = ComplexityType.LINEAR

    enum class ComplexityType {
        STANDARD {
            override fun evaluate(edges: Int, nodes: Int, connections: Int): Int {
                return edges - nodes + connections * 2
            }
        },
        LINEAR {
            override fun evaluate(edges: Int, nodes: Int, connections: Int): Int {
                return edges - nodes + connections
            }
        },
        SIMPLE {
            override fun evaluate(edges: Int, nodes: Int, connections: Int): Int {
                return edges - nodes + 2
            }
        };

        open fun evaluate(edges: Int, nodes: Int, connections: Int): Int {
            throw IllegalArgumentException("No type specified")
        }
    }

    /**
     * Sets the complexity to standard.
     *
     * @return the complexity with standard evaluation.
     */
    fun standard(): CyclomaticComplexity {
        type = ComplexityType.STANDARD
        return this
    }

    /**
     * Sets the complexity to linear.
     *
     * @return the complexity with linear evaluation.
     */
    fun linear(): CyclomaticComplexity {
        type = ComplexityType.LINEAR
        return this
    }

    /**
     * Sets the complexity to simple.
     *
     * @return the complexity with simple evaluation.
     */
    fun simple(): CyclomaticComplexity {
        type = ComplexityType.SIMPLE
        return this
    }

    /**
     * Gets the edge count.
     *
     * @return the edge count.
     */
    fun edges(): Int {
        return edges
    }

    /**
     * Gets the node count.
     *
     * @return the node count.
     */
    fun nodes(): Int {
        return nodes
    }

    /**
     * Gets the connection count.
     *
     * @return the connection count.
     */
    fun connections(): Int {
        return connections
    }

    /**
     * Gets the complexity evaluation.
     *
     * @return the complexity evaluation.
     */
    fun complexity(): Int {
        return type.evaluate(edges, nodes, connections)
    }

    /**
     * Checks if the complexity is within the two given values.
     *
     * @param low  The lowest range value.
     * @param high The highest range value.
     * @return <t>true</t> if the complexity is within the two given values,
     * otherwise <t>false</t>.
     */
    fun within(low: Int, high: Int): Boolean {
        val complexity = complexity()
        return complexity >= low && complexity <= high
    }

    /**
     * Checks if the complexity is less than the given value.
     *
     * @param x the value to check against.
     * @return <t>true</t> if the complexity is less than the given value,
     * otherwise <t>false</t>.
     */
    fun less(x: Int): Boolean {
        return complexity() < x
    }

    /**
     * Checks if the complexity is greater than the given value.
     *
     * @param x the value to check against.
     * @return <t>true</t> if the complexity is greater than the given value,
     * otherwise <t>false</t>.
     */
    fun greater(x: Int): Boolean {
        return complexity() > x
    }

    /**
     * Checks if the complexity equals the given value.
     *
     * @param x the value to check against.
     * @return <t>true</t> if the complexity equals the given value, otherwise
     * <t>false</t>.
     */
    fun `is`(x: Int): Boolean {
        return complexity() == x
    }

    override fun toString(): String {
        return String.format("cyclomatic[edges=%s][nodes=%s][connections=%s][complexity=%s/%s]", edges, nodes,
                connections, type.toString(), complexity())
    }
}