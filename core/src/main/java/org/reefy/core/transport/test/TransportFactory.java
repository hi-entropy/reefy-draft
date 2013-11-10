package org.reefy.core.transport.test;

import org.reefy.core.AppServerHandler;
import org.reefy.core.transport.TransportServer;
import org.reefy.core.transport.Contact;
import org.reefy.core.transport.TransportClient;

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
