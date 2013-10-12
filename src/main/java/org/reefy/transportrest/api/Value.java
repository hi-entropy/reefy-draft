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
    public byte[] getBytes();
}
