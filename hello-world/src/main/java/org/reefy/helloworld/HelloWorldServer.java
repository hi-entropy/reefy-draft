package org.reefy.helloworld;

import org.reefy.storesqlite.SqliteStore;
import org.reefy.transportrest.RestContact;
import org.reefy.transportrest.RestTransportServer;
import org.reefy.core.AppServerHandler;
import org.reefy.core.RawKey;
import org.reefy.core.SimpleAppServer;
import org.reefy.core.store.Store;
import org.reefy.core.store.StoreException;
import org.reefy.core.transport.TransportServer;
import org.reefy.core.transport.TransportServerFactory;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class HelloWorldServer {
    public static void main(String[] args) throws StoreException {
        final Store store = new SqliteStore("helloworld");
        final TransportServerFactory transportServerFactory = new TransportServerFactory() {
            @Override
            public TransportServer build(AppServerHandler handler) {
                return new RestTransportServer(
                    new RestContact(RawKey.pseudorandom(), "localhost", 8000), handler);
            }
        };
        final SimpleAppServer simpleAppServer = new SimpleAppServer(store, transportServerFactory);

        simpleAppServer.startAndWait();
        simpleAppServer.clear();
    }
}
