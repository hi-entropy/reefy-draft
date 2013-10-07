package org.reefy.test;

import org.reefy.transportrest.api.AppServerHandler;
import org.reefy.transportrest.api.TransportServer;
import org.reefy.transportrest.api.transport.Contact;
import org.reefy.transportrest.api.transport.TransportClient;

import java.util.Map;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public interface TransportFactory<C extends Contact> {
    public TransportClient<C> buildClient();

    public ServerWhatever<C> buildServer(AppServerHandler handler);

    public interface ServerWhatever<C extends Contact> {
        public C getContact();

        public TransportServer<C> getServer();
    }
}
