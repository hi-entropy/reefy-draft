package org.reefy.transportrest.api.transport;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class HopsExceededException extends Exception {
    public HopsExceededException() {
        super("Max number of hops exceeded");
    }
}
