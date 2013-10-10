package org.reefy.transportrest.api;

import org.reefy.transportrest.api.transport.Contact;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class AbstractContact implements Contact {
    private final Key key;

    public AbstractContact(Key key) {
        this.key = key;
    }

    @Override
    public Key getKey() {
        return key;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (! (obj instanceof Contact)) { return false; }

        return this.key.equals(((Contact) obj).getKey());
    }

    @Override
    public String toString() {
        return "AbstractContact{" +
               "key=" + key +
               '}';
    }
}
