package org.reefy.storesqlite;

import org.reefy.transportrest.api.Key;
import org.reefy.transportrest.api.Value;
import org.reefy.transportrest.api.store.Store;

public class SqliteStore implements Store {

    @Override
    public <V> void put(Key key, Value<V> value, PutCallback<V> callback) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void close() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <V> void get(Key key, GetCallback<V> callback) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}