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
public abstract class TypedValue<V> implements Value<V> {

    public final V data;

    /**
     * @param data
     */
    public TypedValue(V data) {
        this.data = data;
    }

    /**
     * Makes a defensive copy
     *
     * @param data
     */
    public TypedValue(ByteBuffer data) {
        final ByteBuffer thisDataMutable = ByteBuffer.allocate(data.limit());
        thisDataMutable.put(data);
        this.data = this.deserialize(data);
    }

    /**
     * Makes a defensive copy
     *
     * @param data
     */
    public TypedValue(byte[] data) {
        this(ByteBuffer.allocate(data.length).put(data));
    }

    @Override
    public final ByteBuffer getBytes() {
        return this.serialize();
    }

    @Override
    public final V get() {
        return data;
    }

    protected abstract V deserialize(ByteBuffer data);

    protected abstract ByteBuffer serialize();

}