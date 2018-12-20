package org.objectweb.asm.commons.cfg;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.cfg.*;
import org.objectweb.asm.commons.cfg.graph.Digraph;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.*;

import java.util.*;

import static org.objectweb.asm.Opcodes.*;

/**
 * @author Tyler Sedlar
 */
public class FlowVisitor extends MethodVisitor {

    private MethodNode mn;
    private org.objectweb.asm.commons.cfg.Block current = new org.objectweb.asm.commons.cfg.Block(new Label());
    public final List<org.objectweb.asm.commons.cfg.Block> blocks = new ArrayList<>();
    public final Digraph<org.objectweb.asm.commons.cfg.Block, org.objectweb.asm.commons.cfg.Block> graph = new Digraph<>();

    public void accept(MethodNode mn) {
        current = new org.objectweb.asm.commons.cfg.Block(new Label());
        blocks.clear();
        blocks.add(current);
        graph.flush();
        (this.mn = mn).accept(this);
    }

    /**
     * Constructs blocks for all given labels.
     *
     * @param labels The labels in which to construct blocks for.
     * @return The constructed blocks.
     */
    private org.objectweb.asm.commons.cfg.Block[] constructAll(List<LabelNode> labels) {
        org.objectweb.asm.commons.cfg.Block[] blocks = new org.objectweb.asm.commons.cfg.Block[labels.size()];
        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = construct(labels.get(i));
        }
        return blocks;
    }

    /**
     * Constructs a block for the given label.
     *
     * @param ln The label to get a block from.
     * @return The given label's block.
     */
    private org.objectweb.asm.commons.cfg.Block construct(LabelNode ln) {
        return construct(ln, true);
    }

    /**
     * Constructs a block for the given label.
     *
     * @param ln  The label to get a block from.
     * @param add true to add the block to the next preds, otherwise false.
     * @return A block for the given label.
     */
    private org.objectweb.asm.commons.cfg.Block construct(LabelNode ln, boolean add) {
        Label label = ln.getLabel();
        if (!(label.info instanceof org.objectweb.asm.commons.cfg.Block)) {
            label.info = new org.objectweb.asm.commons.cfg.Block(label);
            if (add) {
                current.next = ((org.objectweb.asm.commons.cfg.Block) label.info);
                current.next.preds.add(current.next);
            }
            blocks.add((org.objectweb.asm.commons.cfg.Block) label.info);
        }
        return (org.objectweb.asm.commons.cfg.Block) label.info;
    }

    @Override
    public void visitInsn(InsnNode in) {
        current.instructions.add(in);
        switch (in.opcode()) {
            case RETURN:
            case IRETURN:
            case ARETURN:
            case FRETURN:
            case DRETURN:
            case LRETURN:
            case ATHROW: {
                current = construct(new LabelNode(new Label()), false);
                break;
            }
        }
    }

    @Override
    public void visitIntInsn(IntInsnNode iin) {
        current.instructions.add(iin);
    }

    @Override
    public void visitVarInsn(VarInsnNode vin) {
        current.instructions.add(vin);
    }

    @Override
    public void visitTypeInsn(TypeInsnNode tin) {
        current.instructions.add(tin);
    }

    @Override
    public void visitFieldInsn(FieldInsnNode fin) {
        current.instructions.add(fin);
    }

    @Override
    public void visitMethodInsn(MethodInsnNode min) {
        current.instructions.add(min);
    }

    @Override
    public void visitInvokeDynamicInsn(InvokeDynamicInsnNode idin) {
        current.instructions.add(idin);
    }

    @Override
    public void visitJumpInsn(JumpInsnNode jin) {
        int opcode = jin.opcode();
        current.target = construct(jin.label);
        current.target.preds.add(current.target);
        if (opcode != GOTO) {
            current.instructions.add(jin);
        }
        Stack<AbstractInsnNode> stack = current.stack;
        current = construct(new LabelNode(new Label()), opcode != GOTO);
        current.stack = stack;
    }

    @Override
    public void visitLabel(Label label) {
        if (label == null || label.info == null) {
            return;
        }
        Stack<AbstractInsnNode> stack = current == null ? new Stack<AbstractInsnNode>() : current.stack;
        current = construct(new LabelNode(label));
        current.stack = stack;
    }

    @Override
    public void visitLdcInsn(LdcInsnNode ldc) {
        current.instructions.add(ldc);
    }

    @Override
    public void visitIincInsn(IincInsnNode iin) {
        current.instructions.add(iin);
    }

    @Override
    public void visitTableSwitchInsn(TableSwitchInsnNode tsin) {
        construct(tsin.dflt);
        constructAll(tsin.labels);
        current.instructions.add(tsin);
    }

    @Override
    public void visitLookupSwitchInsn(LookupSwitchInsnNode lsin) {
        construct(lsin.dflt);
        constructAll(lsin.labels);
        current.instructions.add(lsin);
    }

    @Override
    public void visitMultiANewArrayInsn(MultiANewArrayInsnNode manain) {
        current.instructions.add(manain);
    }

    @Override
    public void visitEnd() {
        List<org.objectweb.asm.commons.cfg.Block> empty = new ArrayList<>();
        for (org.objectweb.asm.commons.cfg.Block block : blocks) {
            block.owner = mn;
            if (block.isEmpty()) {
                empty.add(block);
            }
        }
        blocks.removeAll(empty);
        Collections.sort(blocks, new Comparator<org.objectweb.asm.commons.cfg.Block>() {
            public int compare(org.objectweb.asm.commons.cfg.Block b1, org.objectweb.asm.commons.cfg.Block b2) {
                return mn.instructions.indexOf(new LabelNode(b1.label)) - mn.instructions.indexOf(new LabelNode(b2.label));
            }
        });
        for (org.objectweb.asm.commons.cfg.Block block : blocks) {
            block.setIndex(blocks.indexOf(block));
            if (!graph.containsVertex(block)) {
                graph.addVertex(block);
            }
            if (block.target != null && block.target != block) {
                if (!graph.containsVertex(block.target)) {
                    graph.addVertex(block.target);
                }
                graph.addEdge(block, block.target);
            }
            if (block.next != null) {
                if (!graph.containsVertex(block.next)) {
                    graph.addVertex(block.next);
                }
                graph.addEdge(block, block.next);
            }
        }
    }
}
