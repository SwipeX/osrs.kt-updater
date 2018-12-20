package org.objectweb.asm.tree;

/**
 * @author Tyler Sedlar
 */
public class ParameterAnnotationNode {

    /**
     * The parameter of the annotation class.
     */
    public int parameter;

    /**
     * The class descriptor of the annotation class.
     */
    public String desc;

    /**
     * Constructs a new {@link org.objectweb.asm.tree.AnnotationNode}.
     *
     * @param parameter the parameter of the annotation class
     * @param desc      the class descriptor of the annotation class.
     */
    public ParameterAnnotationNode(final int parameter, final String desc) {
        this.parameter = parameter;
        this.desc = desc;
    }
}
