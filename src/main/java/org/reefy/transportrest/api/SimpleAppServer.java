package org.reefy.transportrest.api;

import com.google.common.util.concurrent.AbstractService;
import org.reefy.transportrest.api.store.Store;
import org.reefy.transportrest.api.store.StoreException;
import org.reefy.transportrest.api.transport.TransportServer;
import org.reefy.transportrest.api.transport.TransportServerFactory;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class SimpleAppServer extends AbstractService {
    private final Store store;
    private final AppServerHandler handler;
    private final TransportServer transportServer;

    public SimpleAppServer(Store store, TransportServerFactory transportServer) {
        this.store = store;
        this.handler = new SimpleAppServerHandler(store);
        this.transportServer = transportServer.build(this.handler);
    }

    @Override
    protected void doStart() {
        store.startAndWait();
        transportServer.startAndWait();
    }

    @Override
    protected void doStop() {
        store.stopAndWait();
        transportServer.stopAndWait();
    }

    public void clear() throws StoreException {
        this.store.clear();
    }
}
