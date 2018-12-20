package org.objectweb.asm.commons.util;

/**
 * @author Tyler Sedlar
 * @since 3/19/15.
 */
public interface Filter<E> {

    public boolean accept(E e);
}
