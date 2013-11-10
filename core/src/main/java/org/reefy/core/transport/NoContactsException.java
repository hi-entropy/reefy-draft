package org.reefy.core.transport;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class NoContactsException extends Exception {
    public NoContactsException() {
        super("No contacts found");
    }
}
