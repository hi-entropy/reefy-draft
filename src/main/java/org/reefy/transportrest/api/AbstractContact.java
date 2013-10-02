package org.reefy.transportrest.api;

import com.google.common.base.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;
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
        if (! (obj instanceof AbstractContact)) { return false; }

        return this.key.equals(((AbstractContact) obj).key);
    }
}
