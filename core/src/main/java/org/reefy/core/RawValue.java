package org.reefy.core;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;

/**
 * A value that's just a simple string of bytes with no fixed semantic meaning.
 *
 * All constructors make defensive copies of inputs.
 *
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class RawValue implements Value {

    private final byte[] bytes;

    public RawValue(byte[] bytes) {
        this.bytes = Arrays.copyOf(bytes, bytes.length);
    }

    @Override
    public byte[] getBytes() {
        return Arrays.copyOf(bytes, bytes.length);
    }

    public static RawValue pseudorandom(int length) {
        final byte[] data = new byte[length];
        new Random().nextBytes(data);
        return new RawValue(data);
    }

    @Override
    public int hashCode() {
        return bytes.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (! (obj instanceof Value)) { return false; }

        return Arrays.equals(bytes, (((Value) obj).getBytes()));
    }

    @Override
    public String toString() {
        return new String(bytes, StandardCharsets.ISO_8859_1);
    }
}