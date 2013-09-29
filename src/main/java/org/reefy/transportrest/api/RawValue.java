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
public class RawValue implements Value {

    public final ByteBuffer data;

    /**
     * Makes a defensive copy
     *
     * @param data
     */
    public RawValue(ByteBuffer data) {
        final ByteBuffer thisDataMutable = ByteBuffer.allocate(data.limit());
        thisDataMutable.put(data);
        this.data = thisDataMutable.asReadOnlyBuffer();
    }

    /**
     * Makes a defensive copy
     *
     * @param data
     */
    public RawValue(byte[] data) {
        this(ByteBuffer.allocate(data.length).put(data));
    }

    public static RawValue pseudorandom(int length) {
        final byte[] data = new byte[length];
        new Random().nextBytes(data);
        return new RawValue(data);
    }

    @Override
    public ByteBuffer getBytes() {
        return this.data;
    }
}