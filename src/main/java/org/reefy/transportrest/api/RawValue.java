package org.reefy.transportrest.api;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

/**
 * A value that's just a simple string of bytes with no fixed semantic meaning.
 *
 * All constructors make defensive copies of inputs.
 *
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class RawValue implements Value {

    private final ByteBuffer data;

    public RawValue(byte[] data) {
        this.data = ByteBuffer.wrap(data);
    }

    @Override
    public ByteBuffer getBytes() {
        return data;
    }

    public static RawValue pseudorandom(int length) {
        final byte[] data = new byte[length];
        new Random().nextBytes(data);
        return new RawValue(data);
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (! (obj instanceof Value)) { return false; }

        return this.data.equals(((Value) obj).getBytes());
    }

    @Override
    public String toString() {
        return new String(data.array(), Charset.forName("ISO-8859-1"));
    }
}