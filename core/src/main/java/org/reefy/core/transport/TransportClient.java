package org.reefy.core.transport;

import com.google.common.util.concurrent.Service;

import org.reefy.core.Key;
import org.reefy.core.Value;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public interface TransportClient<C extends Contact> extends Service {
    public void find(C contact, Key key, FindCallback<C> callback);

    public void get(C contact, Key key, GetCallback<C> callback);

    public void put(C contact, Key key, Value value, PutCallback callback);

    public static interface FindCallback<C extends Contact> {
        public void succeed();

        void redirect(C contact);
    }

    public static interface GetCallback<C extends Contact> {
        public void present(Value value);

        void redirect(C contact);

        public void fail(TransportException exception);
    }

    public static interface PutCallback<C extends Contact> {
        public void succeed();

        void redirect(C contact);

        public void fail(TransportException exception);
    }
}
