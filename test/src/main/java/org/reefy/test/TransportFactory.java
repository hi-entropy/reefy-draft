package org.reefy.test;

import org.reefy.transportrest.api.TransportServer;
import org.reefy.transportrest.api.transport.Contact;
import org.reefy.transportrest.api.transport.TransportClient;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public interface TransportFactory<C extends Contact> {
    TransportServer<C> buildServer();

    TransportClient<C> buildClient();
}
