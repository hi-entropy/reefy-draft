package org.reefy.core.transport;

import org.reefy.core.transport.TransportException;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class ContactNotFoundException extends TransportException {
    public ContactNotFoundException() {
        super("No contacts were found.");
    }
}
