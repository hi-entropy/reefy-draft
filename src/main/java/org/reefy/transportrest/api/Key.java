package org.reefy.transportrest.api;

import java.nio.ByteBuffer;

/**
 * This represents a piece of data that can be stored on a single machine.
 *
 * @author Paul Kernfeld - pk@knewton.com
 */
public interface Key {
    public ByteBuffer getBytes();
}
