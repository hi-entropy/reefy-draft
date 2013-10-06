package org.reefy.transportrest.api.transport;

import org.reefy.transportrest.api.transport.TransportException;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class ContactNotFoundException extends TransportException {
    public ContactNotFoundException() {
        super("No contacts were found.");
    }
}
