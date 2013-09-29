package org.reefy.transportrest.api.transport;

import org.reefy.transportrest.api.Key;
import org.reefy.transportrest.api.Value;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public interface TransportClient<C extends Contact> {

    public void get(C contact, Key key, GetCallback callback);

    public void put(C contact, Key key, Value value, PutCallback callback);

    public static interface GetCallback {
        public void succeed(Value value);

        public void fail(TransportException exception);
    }

    public static interface PutCallback {

    }
}
