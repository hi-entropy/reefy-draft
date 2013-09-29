package org.reefy.transportrest.api;

import java.nio.ByteBuffer;

/**
 * Should be immutable.
 *
 *
 *
 * @author Paul Kernfeld - pk@knewton.com
 */
public interface Value {

    /**
     * Should be read-only
     */
    public ByteBuffer getBytes();
}
