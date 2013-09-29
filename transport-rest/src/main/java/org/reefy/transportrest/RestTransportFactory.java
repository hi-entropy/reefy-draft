package org.reefy.transportrest;

import org.reefy.test.TransportFactory;
import org.reefy.transportrest.api.TransportServer;
import org.reefy.transportrest.api.transport.TransportClient;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class RestTransportFactory implements TransportFactory<RestContact> {
    @Override
    public TransportServer<RestContact> buildServer() {
        return new RestTransportServer();
    }

    @Override
    public TransportClient<RestContact> buildClient() {
        return new RestTransportClient();
    }
}
