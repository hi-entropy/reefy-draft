package org.reefy.test;

import org.reefy.transportrest.api.TransportServer;
import org.reefy.transportrest.api.transport.TransportClient;
import org.reefy.transportrest.api.transport.local.LocalContact;

import java.util.Map;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class InMemoryIntegrationTest extends AbstractIntegrationTest<LocalContact> {
    public InMemoryIntegrationTest() {
        super(new TransportFactory<LocalContact>() {
            @Override
            public Map.Entry<LocalContact, TransportServer<LocalContact>> buildServer() {
                retu
            }

            @Override
            public TransportClient<LocalContact> buildClient() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });

    }
}
