package org.reefy.transportrest.api.store;

import org.reefy.transportrest.api.Key;
import org.reefy.transportrest.api.Value;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class InMemoryStore implements Store {

    @Override
    public <V> void put(Key key, Value<V> value, PutCallback<V> callback) {
    }

    @Override
    public <T> void get(Key key, GetCallback<T> callback) {

    }
}
