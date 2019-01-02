package kt.osrs.analysis.tree

import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

open class Tree<E : Tree<E>> : CopyOnWriteArrayList<E>() {

    protected var parent: Tree<E>? = null

    fun addFirst(e: E) {
        val list = ArrayList<E>()
        for (element in this) {
            list.add(element)
        }
        clear()
        e.parent = this
        add(e)
        addAll(list)
    }

    operator fun set(predecessor: E, successor: E) {
        val it = parent!!.iterator()
        val es = LinkedList<E>()
        while (it.hasNext()) {
            val e = it.next()
            if (e == predecessor) {
                es.add(successor)
            } else {
                es.add(e)
            }
        }
        parent!!.clear()
        parent!!.addAll(es)
    }

    fun parent(): E? {
        return parent as E?
    }

    fun hasParent(): Boolean {
        return parent() != null
    }

    fun previous(): E? {
        val p = parent ?: return null
        val it = parent!!.iterator()
        var prev: E? = null
        while (it.hasNext()) {
            val e = it.next()
            if (e == this) {
                return prev
            }
            prev = e
        }
        return null
    }

    fun hasPrevious(): Boolean {
        return previous() != null
    }

    operator fun next(): E? {
        val p = parent ?: return null
        val it = parent!!.iterator()
        while (it.hasNext()) {
            val e = it.next()
            if (e == this) {
                return if (it.hasNext()) it.next() else null
            }
        }
        return null
    }

    operator fun hasNext(): Boolean {
        return next() != null
    }

    override fun hashCode(): Int {
        var hashCode = 1
        for (e in this) {
            hashCode = 31 * hashCode + (e?.hashCode() ?: 0)
        }
        return hashCode
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as Tree<*>

        if (parent != other.parent) return false

        return true
    }
}