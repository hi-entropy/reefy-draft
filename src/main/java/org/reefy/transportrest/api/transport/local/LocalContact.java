package org.reefy.transportrest.api.transport.local;

import org.reefy.transportrest.api.AbstractContact;
import org.reefy.transportrest.api.Key;

/**
 * @author Paul Kernfeld <pk@knewton.com>
 */
public class LocalContact extends AbstractContact {
    public LocalContact(Key key) {
        super(key);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
