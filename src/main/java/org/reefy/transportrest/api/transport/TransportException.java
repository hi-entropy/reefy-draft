package org.reefy.transportrest.api.transport;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class TransportException extends Exception {
    public TransportException(String message) {
        super(message);
    }

    public TransportException(Exception e) {
        super(e);
    }
}
