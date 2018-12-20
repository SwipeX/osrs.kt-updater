package kt.osrs.analysis.tree

import kt.osrs.analysis.tree.node.*
import org.objectweb.asm.Opcodes

class NodeVisitor : Opcodes {

    private var nv: NodeVisitor? = null

    fun validate(): Boolean {
        return nv == null
    }

    constructor() {
        this.nv = null
    }

    constructor(nv: NodeVisitor) {
        this.nv = nv
    }

    fun visitAny(n: AbstractNode) {
        if (nv != null) {
            nv!!.visitAny(n)
        }
    }

    fun visit(n: AbstractNode) {
        if (nv != null) {
            nv!!.visit(n)
        }
    }

    fun visitCode() {
        if (nv != null) {
            nv!!.visitCode()
        }
    }

    fun visitEnd() {
        if (nv != null) {
            nv!!.visitEnd()
        }
    }

    fun visitField(fmn: FieldMemberNode) {
        if (nv != null) {
            nv!!.visitField(fmn)
        }
    }

    fun visitFrame(n: AbstractNode) {
        if (nv != null) {
            nv!!.visitFrame(n)
        }
    }

    fun visitIinc(`in`: IincNode) {
        if (nv != null) {
            nv!!.visitIinc(`in`)
        }
    }

    fun visitJump(jn: JumpNode) {
        if (nv != null) {
            nv!!.visitJump(jn)
        }
    }

    fun visitLabel(n: AbstractNode) {
        if (nv != null) {
            nv!!.visitLabel(n)
        }
    }

    fun visitConversion(cn: ConversionNode) {
        if (nv != null) {
            nv!!.visitConversion(cn)
        }
    }

    fun visitConstant(cn: ConstantNode) {
        if (nv != null) {
            nv!!.visitConstant(cn)
        }
    }

    fun visitNumber(nn: NumberNode) {
        if (nv != null) {
            nv!!.visitNumber(nn)
        }
    }

    fun visitOperation(an: ArithmeticNode) {
        if (nv != null) {
            nv!!.visitOperation(an)
        }
    }

    fun visitVariable(vn: VariableNode) {
        if (nv != null) {
            nv!!.visitVariable(vn)
        }
    }

    fun visitLine(n: AbstractNode) {
        if (nv != null) {
            nv!!.visitLine(n)
        }
    }

    fun visitLookupSwitch(n: AbstractNode) {
        if (nv != null) {
            nv!!.visitLookupSwitch(n)
        }
    }

    fun visitMethod(mmn: MethodMemberNode) {
        if (nv != null) {
            nv!!.visitMethod(mmn)
        }
    }

    fun visitMultiANewArray(n: AbstractNode) {
        if (nv != null) {
            nv!!.visitMultiANewArray(n)
        }
    }

    fun visitTableSwitch(n: AbstractNode) {
        if (nv != null) {
            nv!!.visitTableSwitch(n)
        }
    }

    fun visitType(tn: TypeNode) {
        if (nv != null) {
            nv!!.visitType(tn)
        }
    }
}
