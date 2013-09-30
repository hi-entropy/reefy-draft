package org.reefy.transportrest.api;

import java.nio.ByteBuffer;
import java.util.Random;

/**
 * A value that's just a simple string of bytes with no fixed semantic meaning.
 *
 * All constructors make defensive copies of inputs.
 *
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class RawValue extends TypedValue<ByteBuffer> {

    public RawValue(byte[] data) {
        super(data);
    }

    @Override
    public ByteBuffer serialize() {
        return this.data;
    }

    @Override
    protected ByteBuffer deserialize(ByteBuffer data) {
        return this.data;
    }

    public static RawValue pseudorandom(int length) {
        final byte[] data = new byte[length];
        new Random().nextBytes(data);
        return new RawValue(data);
    }
}