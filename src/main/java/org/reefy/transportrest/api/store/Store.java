package org.reefy.transportrest.api.store;

import com.google.common.util.concurrent.Service;
import org.reefy.transportrest.api.Key;
import org.reefy.transportrest.api.Value;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public interface Store extends Service {
    public void clear() throws StoreException;

    public <V> void put(Key key, Value<V> value, PutCallback<V> callback);

    public static interface PutCallback<V> {
        public void succeed();

        public void fail(StoreException e);
    }

    public <V> void get(Key key, GetCallback<V> callback);

    public static interface GetCallback<V> {
        public void succeed(Value<V> value);

        public void notFound();

        public void fail(StoreException e);
    }
}
