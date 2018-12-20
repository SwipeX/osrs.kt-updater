package org.objectweb.asm.commons.cfg;

import org.objectweb.asm.commons.cfg.*;

public interface BlockHandler {

    boolean handle(int followIndex, org.objectweb.asm.commons.cfg.Block block);
}