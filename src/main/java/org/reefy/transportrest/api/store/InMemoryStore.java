package org.reefy.transportrest.api.store;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import org.reefy.transportrest.api.Key;
import org.reefy.transportrest.api.TrivialService;
import org.reefy.transportrest.api.Value;

import java.util.concurrent.ConcurrentMap;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class InMemoryStore extends TrivialService implements Store {
    private final ConcurrentMap<Key, Value> keyValueMap = Maps.newConcurrentMap();

    @Override
    public void clear() throws StoreException {
        Preconditions.checkState(this.isRunning());

        keyValueMap.clear();
    }

    @Override
    public <V> void put(Key key, Value<V> value, PutCallback<V> callback) {
        Preconditions.checkState(this.isRunning());

        keyValueMap.put(key, value);
        callback.succeed();
    }

    @Override
    public <T> void get(Key key, GetCallback<T> callback) {
        Preconditions.checkState(this.isRunning());

        final Value gotten = keyValueMap.get(key);
        if (gotten == null) {
            callback.notFound();
        } else {
            callback.succeed(gotten);
        }
    }
}

