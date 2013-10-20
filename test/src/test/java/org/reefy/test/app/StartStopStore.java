package org.reefy.test.app;

import org.reefy.transportrest.api.Key;
import org.reefy.transportrest.api.TrivialIdleService;
import org.reefy.transportrest.api.Value;
import org.reefy.transportrest.api.store.Store;
import org.reefy.transportrest.api.store.StoreException;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class StartStopStore extends TrivialIdleService implements Store {

    @Override
    public void clear() throws StoreException {
        throw new UnsupportedOperationException("This class only starts and stops.");
    }

    @Override
    public <V> void put(Key key, Value<V> value, PutCallback<V> callback) {
        throw new UnsupportedOperationException("This class only starts and stops.");
    }

    @Override
    public <V> void get(Key key, GetCallback<V> callback) {
        throw new UnsupportedOperationException("This class only starts and stops.");
    }
}
