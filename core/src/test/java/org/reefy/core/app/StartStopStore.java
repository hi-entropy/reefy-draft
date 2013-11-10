package org.reefy.core.app;

import org.reefy.core.Key;
import org.reefy.core.TrivialIdleService;
import org.reefy.core.Value;
import org.reefy.core.store.Store;
import org.reefy.core.store.StoreException;

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
