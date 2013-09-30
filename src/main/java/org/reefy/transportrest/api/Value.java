package org.reefy.transportrest.api;

import java.nio.ByteBuffer;

/**
 * Should be immutable.
 *
 *
 *
 * @author Paul Kernfeld - pk@knewton.com
 */
public interface Value<V> {

    /**
     * Should be read-only
     */
    public ByteBuffer getBytes();

    /**
     * Not really sure if this is gonna work...
     * @return
     */
    public V get();
}
