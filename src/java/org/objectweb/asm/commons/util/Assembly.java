package org.objectweb.asm.commons.util;

import org.objectweb.asm.Label;
import org.objectweb.asm.tree.*;

import java.util.*;

import static org.objectweb.asm.tree.AbstractInsnNode.*;

/**
 * @author Tyler Sedlar
 */
public class Assembly {

    public static final String[] OPCODES = {"NOP", "ACONST_NULL", "ICONST_M1", "ICONST_0", "ICONST_1", "ICONST_2", "ICONST_3", "ICONST_4", "ICONST_5", "LCONST_0", "LCONST_1", "FCONST_0", "FCONST_1", "FCONST_2", "DCONST_0", "DCONST_1", "BIPUSH", "SIPUSH", "LDC", "", "", "ILOAD", "LLOAD", "FLOAD", "DLOAD", "ALOAD", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "IALOAD", "LALOAD", "FALOAD", "DALOAD", "AALOAD", "BALOAD", "CALOAD", "SALOAD", "ISTORE", "LSTORE", "FSTORE", "DSTORE", "ASTORE", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "IASTORE", "LASTORE", "FASTORE", "DASTORE", "AASTORE", "BASTORE", "CASTORE", "SASTORE", "POP", "POP2", "DUP", "DUP_X1", "DUP_X2", "DUP2", "DUP2_X1", "DUP2_X2", "SWAP", "IADD", "LADD", "FADD", "DADD", "ISUB", "LSUB", "FSUB", "DSUB", "IMUL", "LMUL", "FMUL", "DMUL", "IDIV", "LDIV", "FDIV", "DDIV", "IREM", "LREM", "FREM", "DREM", "INEG", "LNEG", "FNEG", "DNEG", "ISHL", "LSHL", "ISHR", "LSHR", "IUSHR", "LUSHR", "IAND", "LAND", "IOR", "LOR", "IXOR", "LXOR", "IINC", "I2L", "I2F", "I2D", "L2I", "L2F", "L2D", "F2I", "F2L", "F2D", "D2I", "D2L", "D2F", "I2B", "I2C", "I2S", "LCMP", "FCMPL", "FCMPG", "DCMPL", "DCMPG", "IFEQ", "IFNE", "IFLT", "IFGE", "IFGT", "IFLE", "IF_ICMPEQ", "IF_ICMPNE", "IF_ICMPLT", "IF_ICMPGE", "IF_ICMPGT", "IF_ICMPLE", "IF_ACMPEQ", "IF_ACMPNE", "GOTO", "JSR", "RET", "TABLESWITCH", "LOOKUPSWITCH", "IRETURN", "LRETURN", "FRETURN", "DRETURN", "ARETURN", "RETURN", "GETSTATIC", "PUTSTATIC", "GETFIELD", "PUTFIELD", "INVOKEVIRTUAL", "INVOKESPECIAL", "INVOKESTATIC", "INVOKEINTERFACE", "INVOKEDYNAMIC", "NEW", "NEWARRAY", "ANEWARRAY", "ARRAYLENGTH", "ATHROW", "CHECKCAST", "INSTANCEOF", "MONITORENTER", "MONITOREXIT", "", "MULTIANEWARRAY", "IFNULL", "IFNONNULL"};

    public static List<MethodNode> findMethods(Filter<MethodNode> filter, Map<String, ClassNode> classes) {
        List<MethodNode> methods = new ArrayList<>();
        for (ClassNode cn : classes.values()) {
            for (MethodNode mn : cn.methods) {
                if (filter.accept(mn)) {
                    methods.add(mn);
                }
            }
        }
        return methods;
    }

    public static MethodNode findMethod(Filter<MethodNode> filter, Map<String, ClassNode> classes) {
        for (ClassNode cn : classes.values()) {
            for (MethodNode mn : cn.methods) {
                if (filter.accept(mn)) {
                    return mn;
                }
            }
        }
        return null;
    }

    public static String keyFor(FieldInsnNode fin) {
        return fin.owner + "." + fin.name;
    }

    @SuppressWarnings("unchecked")
    public static <E extends AbstractInsnNode> E previous(AbstractInsnNode ain, Filter<AbstractInsnNode> filter,
                                                          int jumpDist) {
        if (ain == null) {
            return null;
        }
        int jump = 0;
        while ((ain = ain.previous()) != null && (jumpDist == -1 || jump++ <= jumpDist)) {
            if (filter.accept(ain)) {
                return (E) ain;
            }
        }
        return null;
    }

    public static <E extends AbstractInsnNode> E previous(AbstractInsnNode ain, Filter<AbstractInsnNode> filter) {
        return previous(ain, filter, 15);
    }

    public static <E extends AbstractInsnNode> E previous(AbstractInsnNode ain, int opcode, int jumpDist) {
        return previous(ain, insn -> insn.opcode() == opcode, jumpDist);
    }

    public static <E extends AbstractInsnNode> E previous(AbstractInsnNode ain, int opcode) {
        return previous(ain, opcode, 15);
    }

    @SuppressWarnings("unchecked")
    public static <E extends AbstractInsnNode> E next(AbstractInsnNode ain, Filter<AbstractInsnNode> filter,
                                                      int jumpDist) {
        if (ain == null) {
            return null;
        }
        int jump = 0;
        while ((ain = ain.next()) != null && (jumpDist == -1 || jump++ <= jumpDist)) {
            if (filter.accept(ain)) {
                return (E) ain;
            }
        }
        return null;
    }

    public static int count(int opcode, MethodNode mn) {
        int count = 0;
        for (AbstractInsnNode ain : mn.instructions.toArray())
            count += (ain.opcode() == opcode ? 1 : 0);
        return count;
    }

    public static <E extends AbstractInsnNode> E next(AbstractInsnNode ain, Filter<AbstractInsnNode> filter) {
        return next(ain, filter, 15);
    }

    public static <E extends AbstractInsnNode> E next(AbstractInsnNode ain, int opcode, int jumpDist) {
        return next(ain, insn -> insn.opcode() == opcode, jumpDist);
    }

    public static <E extends AbstractInsnNode> E next(AbstractInsnNode ain, int opcode) {
        return next(ain, opcode, 15);
    }


    @SuppressWarnings("unchecked")
    public static <E extends AbstractInsnNode> E first(InsnList insns, Filter<AbstractInsnNode> filter) {
        for (AbstractInsnNode ain : insns.toArray()) {
            if (filter.accept(ain)) {
                return (E) ain;
            }
        }
        return null;
    }

    public static boolean instructionsEqual(AbstractInsnNode insn1, AbstractInsnNode insn2) {
        if (insn1 == insn2) {
            return true;
        }
        if (insn1 == null || insn2 == null || insn1.type() != insn2.type() ||
                insn1.opcode() != insn2.opcode()) {
            return false;
        }
        int size;
        switch (insn1.type()) {
            case INSN:
                return true;
            case INT_INSN:
                IntInsnNode iin1 = (IntInsnNode) insn1, iin2 = (IntInsnNode) insn2;
                return iin1.operand == iin2.operand;
            case VAR_INSN:
                VarInsnNode vin1 = (VarInsnNode) insn1, vin2 = (VarInsnNode) insn2;
                return vin1.var == vin2.var;
            case TYPE_INSN:
                TypeInsnNode tin1 = (TypeInsnNode) insn1, tin2 = (TypeInsnNode) insn2;
                return tin1.desc.equals(tin2.desc);
            case FIELD_INSN:
                FieldInsnNode fin1 = (FieldInsnNode) insn1, fin2 = (FieldInsnNode) insn2;
                return fin1.desc.equals(fin2.desc) && fin1.name.equals(fin2.name) && fin1.owner.equals(fin2.owner);
            case METHOD_INSN:
                MethodInsnNode min1 = (MethodInsnNode) insn1, min2 = (MethodInsnNode) insn2;
                return min1.desc.equals(min2.desc) && min1.name.equals(min2.name) && min1.owner.equals(min2.owner);
            case INVOKE_DYNAMIC_INSN:
                InvokeDynamicInsnNode idin1 = (InvokeDynamicInsnNode) insn1, idin2 = (InvokeDynamicInsnNode) insn2;
                return idin1.bsm.equals(idin2.bsm) && Arrays.equals(idin1.bsmArgs, idin2.bsmArgs) &&
                        idin1.desc.equals(idin2.desc) && idin1.name.equals(idin2.name);
            case JUMP_INSN:
                JumpInsnNode jin1 = (JumpInsnNode) insn1, jin2 = (JumpInsnNode) insn2;
                return instructionsEqual(jin1.label, jin2.label);
            case LABEL:
                Label label1 = ((LabelNode) insn1).getLabel(), label2 = ((LabelNode) insn2).getLabel();
                return label1 == null ? label2 == null : label1.info == null ? label2.info == null :
                        label1.info.equals(label2.info);
            case LDC_INSN:
                LdcInsnNode lin1 = (LdcInsnNode) insn1, lin2 = (LdcInsnNode) insn2;
                return lin1.cst.equals(lin2.cst);
            case IINC_INSN:
                IincInsnNode iiin1 = (IincInsnNode) insn1, iiin2 = (IincInsnNode) insn2;
                return iiin1.incr == iiin2.incr && iiin1.var == iiin2.var;
            case TABLESWITCH_INSN:
                TableSwitchInsnNode tsin1 = (TableSwitchInsnNode) insn1, tsin2 = (TableSwitchInsnNode) insn2;
                size = tsin1.labels.size();
                if (size != tsin2.labels.size()) {
                    return false;
                }
                for (int i = 0; i < size; i++) {
                    if (!instructionsEqual(tsin1.labels.get(i), tsin2.labels.get(i))) {
                        return false;
                    }
                }
                return instructionsEqual(tsin1.dflt, tsin2.dflt) && tsin1.max == tsin2.max && tsin1.min == tsin2.min;
            case LOOKUPSWITCH_INSN:
                LookupSwitchInsnNode lsin1 = (LookupSwitchInsnNode) insn1, lsin2 = (LookupSwitchInsnNode) insn2;
                size = lsin1.labels.size();
                if (size != lsin2.labels.size()) {
                    return false;
                }
                for (int i = 0; i < size; i++) {
                    if (!instructionsEqual(lsin1.labels.get(i), lsin2.labels.get(i))) {
                        return false;
                    }
                }
                return instructionsEqual(lsin1.dflt, lsin2.dflt) && lsin1.keys.equals(lsin2.keys);
            case MULTIANEWARRAY_INSN:
                MultiANewArrayInsnNode manain1 = (MultiANewArrayInsnNode) insn1, manain2 = (MultiANewArrayInsnNode) insn2;
                return manain1.desc.equals(manain2.desc) && manain1.dims == manain2.dims;
            case FRAME:
                FrameNode fn1 = (FrameNode) insn1, fn2 = (FrameNode) insn2;
                return fn1.local.equals(fn2.local) && fn1.stack.equals(fn2.stack);
            case LINE:
                LineNumberNode lnn1 = (LineNumberNode) insn1, lnn2 = (LineNumberNode) insn2;
                return lnn1.line == lnn2.line && instructionsEqual(lnn1.start, lnn2.start);
        }
        return false;
    }

    public static boolean instructionsEqual(AbstractInsnNode[] insns, AbstractInsnNode[] insns2) {
        if (insns == insns2) {
            return true;
        }
        if (insns == null || insns2 == null) {
            return false;
        }
        int length = insns.length;
        if (insns2.length != length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            AbstractInsnNode insn1 = insns[i], insn2 = insns2[i];
            if (!(insn1 == null ? insn2 == null : instructionsEqual(insn1, insn2))) {
                return false;
            }
        }
        return true;
    }

    public static String toString(AbstractInsnNode insn) {
        if (insn == null) {
            return "null";
        }
        int op = insn.opcode();
        if (op == -1) {
            return insn.toString();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(OPCODES[op]);
        switch (insn.type()) {
            case INT_INSN:
                sb.append(' ');
                sb.append(((IntInsnNode) insn).operand);
                break;
            case VAR_INSN:
                sb.append(' ');
                sb.append('#');
                sb.append(((VarInsnNode) insn).var);
                break;
            case TYPE_INSN:
                sb.append(' ');
                sb.append(((TypeInsnNode) insn).desc);
                break;
            case FIELD_INSN:
                FieldInsnNode fin = (FieldInsnNode) insn;
                sb.append(' ');
                sb.append(fin.owner);
                sb.append('.');
                sb.append(fin.name);
                sb.append(' ');
                sb.append(fin.desc);
                break;
            case METHOD_INSN:
                MethodInsnNode min = (MethodInsnNode) insn;
                sb.append(' ');
                sb.append(min.owner);
                sb.append('.');
                sb.append(min.name);
                sb.append(' ');
                sb.append(min.desc);
                break;
            case JUMP_INSN:
                break;
            case LDC_INSN:
                Object cst = ((LdcInsnNode) insn).cst;
                sb.append(' ');
                sb.append(cst.getClass().getName());
                sb.append(' ');
                sb.append(cst);
                break;
            case IINC_INSN:
                IincInsnNode iin = (IincInsnNode) insn;
                sb.append(' ');
                sb.append('#');
                sb.append(iin.var);
                sb.append(' ');
                sb.append(iin.incr);
                break;
            case TABLESWITCH_INSN:
                break;
            case LOOKUPSWITCH_INSN:
                break;
            case MULTIANEWARRAY_INSN:
                MultiANewArrayInsnNode m = (MultiANewArrayInsnNode) insn;
                sb.append(' ');
                sb.append(m.desc);
                sb.append(' ');
                sb.append(m.dims);
                break;
        }
        return sb.toString();
    }

    public static void rename(Collection<ClassNode> classes, FieldNode fn, String newName) {
        for (ClassNode node : classes) {
            for (MethodNode mn : node.methods) {
                for (AbstractInsnNode ain : mn.instructions.toArray()) {
                    if (ain instanceof FieldInsnNode) {
                        FieldInsnNode fin = (FieldInsnNode) ain;
                        if ((fin.owner.equals(fn.owner.name) || isInherited(classes, fin.owner, fin)) && fin.name.equals(fn.name)) {
                            fin.name = newName;
                        }
                    }
                }
            }
        }
        fn.name = newName;
    }

    public static void rename(Collection<ClassNode> classes, MethodNode methodNode, String newName) {
        for (ClassNode node : classes) {
            for (MethodNode mn : node.methods) {
                for (AbstractInsnNode ain : mn.instructions.toArray()) {
                    if (ain instanceof MethodInsnNode) {
                        MethodInsnNode min = (MethodInsnNode) ain;
                        if ((min.owner.equals(methodNode.owner.name) || isInherited(classes, min.owner, min)) && min.name.equals(methodNode.name)) {
                            min.name = newName;
                        }
                    }
                }
            }
        }
        methodNode.name = newName;
    }

    private static boolean isInherited(Collection<ClassNode> classes, String clazz, FieldInsnNode fin) {
        ClassNode owner = get(classes, clazz);
        if (owner == null) return false;
        if (owner.getField(fin.name, fin.desc) == null) {
            return owner.ownerless() ? false : isInherited(classes, owner.superName, fin);
        }
        return true;
    }

    private static boolean isInherited(Collection<ClassNode> classes, String clazz, MethodInsnNode min) {
        ClassNode owner = get(classes, clazz);
        if (owner == null) return false;
        if (owner.getMethod(min.name, min.desc) == null) {
            return owner.ownerless() ? false : isInherited(classes, owner.superName, min);
        }
        return true;
    }

    private static ClassNode get(Collection<ClassNode> nodes, String name) {
        for (ClassNode node : nodes) {
            if (node.name.equals(name))
                return node;
        }
        return null;
    }

    public static void rename(Collection<ClassNode> classes, ClassNode cn, String newName) {
        for (ClassNode node : classes) {
            if (node.superName.equals(cn.name)) {
                node.superName = newName;
            }
            if (node.interfaces.contains(cn.name)) {
                node.interfaces.remove(cn.name);
                node.interfaces.add(newName);
            }
            for (FieldNode fn : node.fields) {
                if (fn.desc.endsWith("L" + cn.name + ";")) {
                    fn.desc = fn.desc.replace("L" + cn.name + ";", "L" + newName + ";");
                }
            }
            for (MethodNode mn : node.methods) {
                if (mn.desc.contains("L" + cn.name + ";")) {
                    mn.desc = mn.desc.replace("L" + cn.name + ";", "L" + newName + ";");
                }
                for (AbstractInsnNode ain : mn.instructions.toArray()) {
                    if (ain instanceof FieldInsnNode) {
                        FieldInsnNode fin = (FieldInsnNode) ain;
                        if (fin.owner.equals(cn.name)) {
                            fin.owner = newName;
                        }
                    } else if (ain instanceof MethodInsnNode) {
                        MethodInsnNode min = (MethodInsnNode) ain;
                        if (min.owner.equals(cn.name)) {
                            min.owner = newName;
                        }
                    } else if (ain instanceof TypeInsnNode) {
                        TypeInsnNode tin = (TypeInsnNode) ain;
                        if (tin.desc.contains("L" + cn.name + ";")) {
                            tin.desc = tin.desc.replace("L" + cn.name + ";", "L" + newName + ";");
                        } else if (tin.desc.equals(cn.name)) {
                            tin.desc = newName;
                        }
                    } else if (ain instanceof MultiANewArrayInsnNode) {
                        MultiANewArrayInsnNode main = (MultiANewArrayInsnNode) ain;
                        if (main.desc.contains("L" + cn.name + ";")) {
                            main.desc = main.desc.replace("L" + cn.name + ";", "L" + newName + ";");
                        } else if (main.desc.equals(cn.name)) {
                            main.desc = newName;
                        }
                    }
                }
            }
        }
        cn.name = newName;
    }

    public static List<AbstractInsnNode> allSurrounding(AbstractInsnNode ain) {
        List<AbstractInsnNode> insns = new LinkedList<>();
        AbstractInsnNode insn = ain;
        while ((insn = insn.previous()) != null) {
            insns.add(insn);
        }
        insn = ain;
        insns.add(insn);
        while ((insn = insn.next()) != null) {
            insns.add(insn);
        }
        return insns;
    }
}
