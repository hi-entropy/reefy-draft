package org.reefy.transportrest.api;

import com.google.common.base.Preconditions;

import java.nio.ByteBuffer;
import java.util.Random;

/**
 * Immutable
 *
 * All constructors make defensive copies.
 *
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class RawKey implements Key {

    public final ByteBuffer data;

    public RawKey(ByteBuffer data) {
        Preconditions.checkArgument(data.limit() == 20);
        this.data = ByteBuffer.allocate(20);
        this.data.put(data);
    }

    public RawKey(byte[] data) {
        this(ByteBuffer.allocate(20).put(data));
    }

    public static RawKey pseudorandom() {
        final byte[] data = new byte[20];
        new Random().nextBytes(data);
        return new RawKey(data);
    }

    @Override
    public ByteBuffer getBytes() {
        return data;
    }
}
