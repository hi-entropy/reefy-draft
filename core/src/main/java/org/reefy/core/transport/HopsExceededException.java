package org.reefy.core.transport;

import java.util.List;

/**
 * The client should throw this exception if it takes too many network hops to perform an operation.
 *
 * @author Paul Kernfeld - pk@knewton.com
 */
public class HopsExceededException extends Exception {
    private final List<Contact> hops;

    public HopsExceededException(List<Contact> hops) {
        super("Max number of hops exceeded");
        this.hops = hops;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + hops.toString();
    }

    public List<Contact> getHops() {
        return hops;
    }
}
