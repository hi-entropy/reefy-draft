package org.reefy.test.transport;

import org.reefy.transportrest.api.AppServerHandler;
import org.reefy.transportrest.api.transport.TransportServer;
import org.reefy.transportrest.api.transport.Contact;
import org.reefy.transportrest.api.transport.TransportClient;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public interface TransportFactory<C extends Contact> {
    public TransportClient<C> buildClient();

    public ServerWhatever<C> buildServer(AppServerHandler<C> handler);

    public interface ServerWhatever<C extends Contact> {
        public C getContact();

        public TransportServer<C> getServer();
    }

    public C buildMockContact();
}
