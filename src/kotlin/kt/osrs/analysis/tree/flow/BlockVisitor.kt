package kt.osrs.analysis.tree.flow

import org.objectweb.asm.Opcodes

abstract class BlockVisitor : Opcodes {

    abstract fun visit(block: kt.osrs.analysis.tree.flow.Block)

    fun visitEnd() {}
}
