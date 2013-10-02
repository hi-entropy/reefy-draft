package org.reefy.transportrest.api.transport;

import org.reefy.transportrest.api.transport.TransportException;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class ContactNotFoundException extends TransportException {
    public ContactNotFoundException() {
        super("Need to get rid of this class.");
    }
}
