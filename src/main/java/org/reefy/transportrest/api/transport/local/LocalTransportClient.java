package org.reefy.transportrest.api.transport.local;

import org.reefy.transportrest.api.Key;
import org.reefy.transportrest.api.Value;
import org.reefy.transportrest.api.transport.TransportClient;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class LocalTransportClient implements TransportClient<LocalContact> {

    @Override
    public void get(LocalContact contact, Key key, GetCallback callback) {

    }

    @Override
    public void put(LocalContact contact, Key key, Value value, PutCallback callback) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
