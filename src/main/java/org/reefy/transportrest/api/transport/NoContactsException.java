package org.reefy.transportrest.api.transport;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class NoContactsException extends Exception {
    public NoContactsException() {
        super("No contacts found");
    }
}
