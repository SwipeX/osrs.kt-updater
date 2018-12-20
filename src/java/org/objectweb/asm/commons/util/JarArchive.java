package org.objectweb.asm.commons.util;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

/**
 * @author Tyler Sedlar
 */
public class JarArchive {

    private final Map<String, ClassNode> nodes = new HashMap<>();
    private final Map<String, byte[]> resources = new HashMap<>();

    private final File file;
    private Manifest manifest;

    public JarArchive(File file) {
        this.file = file;
    }

    private byte[] inputToBytes(InputStream in) {
        try (ReadableByteChannel inChannel = Channels.newChannel(in)) {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                try (WritableByteChannel outChannel = Channels.newChannel(baos)) {
                    ByteBuffer buffer = ByteBuffer.allocate(4096);
                    while (inChannel.read(buffer) != -1) {
                        buffer.flip();
                        outChannel.write(buffer);
                        buffer.compact();
                    }
                    buffer.flip();
                    while (buffer.hasRemaining()) {
                        outChannel.write(buffer);
                    }
                    return baos.toByteArray();
                }
            }
        } catch (IOException e) {
            return new byte[0];
        }
    }

    public Map<String, ClassNode> build() {
        if (!nodes.isEmpty()) {
            return nodes;
        }
        try {
            JarFile jar = new JarFile(file);
            manifest = jar.getManifest();
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (name.endsWith(".class")) {
                    ClassNode cn = new ClassNode();
                    ClassReader reader = new ClassReader(jar.getInputStream(entry));
                    reader.accept(cn, ClassReader.SKIP_FRAMES | ClassReader.SKIP_DEBUG);
                    nodes.put(name.replace(".class", ""), cn);
                } else {
                    if (!name.equals("META-INF/MANIFEST.MF")) {
                        resources.put(name, inputToBytes(jar.getInputStream(entry)));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error building classes (" + file.getName() + "): ", e.getCause());
        }
        return nodes;
    }

    public void write(File target) {
        try (JarOutputStream output = (manifest != null ? new JarOutputStream(new FileOutputStream(target), manifest) :
                new JarOutputStream(new FileOutputStream(target)))) {
            for (Map.Entry<String, ClassNode> entry : build().entrySet()) {
                output.putNextEntry(new JarEntry(entry.getValue().name.replaceAll("\\.", "/") + ".class"));
                ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                entry.getValue().accept(writer);
                output.write(writer.toByteArray());
                output.closeEntry();
            }
            for (Map.Entry<String, byte[]> entry : resources.entrySet()) {
                output.putNextEntry(new JarEntry(entry.getKey()));
                output.write(entry.getValue());
                output.closeEntry();
            }
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write() {
        write(file);
    }
}
