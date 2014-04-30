package org.reefy.core;

import org.reefy.core.transport.Contact;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class AbstractContact implements Contact {
    private final Key key;

    public AbstractContact(Key key) {
        this.key = key;
    }

    @Override
    public final Key getKey() {
        return key;
    }

    @Override
    public final int hashCode() {
        return key.hashCode();
    }

    @Override
    public final boolean equals(Object obj) {
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
