package org.reefy.test;

import org.junit.Test;
import org.reefy.transportrest.api.transport.Contact;
import org.reefy.transportrest.api.transport.TransportClient;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public abstract class AbstractTransportClientTest<C extends Contact> {

    private final TransportFactory<C> transportFactory;

    protected AbstractTransportClientTest(TransportFactory<C> transportFactory) {
        this.transportFactory = transportFactory;
    }

    @Test
    public void testGet() {
        final TransportClient<C> client = transportFactory.buildClient();

    }
}
