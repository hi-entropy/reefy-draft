package org.reefy.transportrest.api;

import java.math.BigInteger;

/**
 * This represents the identifier for a piece of data that can be stored on a single machine.
 *
 *
 *
 * @author Paul Kernfeld - pk@knewton.com
 */
public interface Key {
    public byte[] getBytes();

    /**
     * This should use Kademlia's XOR distance metric.
     *
     * @param key the other key to calculate the distance to.
     * @return the distance between the two keys
     */
    public BigInteger distance(Key key);
}
