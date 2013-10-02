package org.reefy.transportrest.api;

import org.reefy.transportrest.api.transport.Contact;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public interface TransportServer<C extends Contact> {
    void get(Key key, GetCallback callback);

    public interface GetCallback {
        public void succeed(Value value);

        public void fail();
    }
}
