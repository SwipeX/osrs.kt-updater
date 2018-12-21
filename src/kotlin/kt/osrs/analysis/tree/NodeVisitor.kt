package kt.osrs.analysis.tree

import kt.osrs.analysis.tree.node.*
import org.objectweb.asm.Opcodes

open class NodeVisitor : Opcodes {

    private var nv: NodeVisitor? = null

    open fun validate(): Boolean {
        return nv == null
    }

    constructor() {
        this.nv = null
    }

    constructor(nv: NodeVisitor) {
        this.nv = nv
    }

    open fun visitAny(n: AbstractNode) {
        if (nv != null) {
            nv!!.visitAny(n)
        }
    }

    open fun visit(n: AbstractNode) {
        if (nv != null) {
            nv!!.visit(n)
        }
    }

    open fun visitCode() {
        if (nv != null) {
            nv!!.visitCode()
        }
    }

    open fun visitEnd() {
        if (nv != null) {
            nv!!.visitEnd()
        }
    }

    open fun visitField(fmn: FieldMemberNode) {
        if (nv != null) {
            nv!!.visitField(fmn)
        }
    }

    open fun visitFrame(n: AbstractNode) {
        if (nv != null) {
            nv!!.visitFrame(n)
        }
    }

    open fun visitIinc(`in`: IincNode) {
        if (nv != null) {
            nv!!.visitIinc(`in`)
        }
    }

    open fun visitJump(jn: JumpNode) {
        if (nv != null) {
            nv!!.visitJump(jn)
        }
    }

    open fun visitLabel(n: AbstractNode) {
        if (nv != null) {
            nv!!.visitLabel(n)
        }
    }

    open fun visitConversion(cn: ConversionNode) {
        if (nv != null) {
            nv!!.visitConversion(cn)
        }
    }

    open fun visitConstant(cn: ConstantNode) {
        if (nv != null) {
            nv!!.visitConstant(cn)
        }
    }

    open fun visitNumber(nn: NumberNode) {
        if (nv != null) {
            nv!!.visitNumber(nn)
        }
    }

    open fun visitOperation(an: ArithmeticNode) {
        if (nv != null) {
            nv!!.visitOperation(an)
        }
    }

    open fun visitVariable(vn: VariableNode) {
        if (nv != null) {
            nv!!.visitVariable(vn)
        }
    }

    open fun visitLine(n: AbstractNode) {
        if (nv != null) {
            nv!!.visitLine(n)
        }
    }

    open fun visitLookupSwitch(n: AbstractNode) {
        if (nv != null) {
            nv!!.visitLookupSwitch(n)
        }
    }

    open fun visitMethod(mmn: MethodMemberNode) {
        if (nv != null) {
            nv!!.visitMethod(mmn)
        }
    }

    open fun visitMultiANewArray(n: AbstractNode) {
        if (nv != null) {
            nv!!.visitMultiANewArray(n)
        }
    }

    open fun visitTableSwitch(n: AbstractNode) {
        if (nv != null) {
            nv!!.visitTableSwitch(n)
        }
    }

    open fun visitType(tn: TypeNode) {
        if (nv != null) {
            nv!!.visitType(tn)
        }
    }
}
