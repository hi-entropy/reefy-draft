package org.reefy.transportrest.api.transport;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public abstract class TransportException extends Exception {
    public TransportException(String message) {
        super(message);
    }
}
