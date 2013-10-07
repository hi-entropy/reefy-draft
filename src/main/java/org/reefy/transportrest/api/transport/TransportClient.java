package org.reefy.transportrest.api.transport;

import com.google.common.util.concurrent.Service;

import org.reefy.transportrest.api.Key;
import org.reefy.transportrest.api.Value;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public interface TransportClient<C extends Contact> extends Service {
    public void find(C contact, Key key, FindCallback callback);

    public void get(C contact, Key key, GetCallback callback);

    public void put(C contact, Key key, Value value, PutCallback callback);

    public static interface FindCallback {
        public void succeed();

        void redirect(Contact contact);
    }

    public static interface GetCallback {
        public void present(Value value);

        void redirect(Contact contact);

        public void notFound();

        public void fail(TransportException exception);
    }

    public static interface PutCallback {
        public void succeed();

        void redirect(Contact contact);

        public void fail(TransportException exception);
    }
}
