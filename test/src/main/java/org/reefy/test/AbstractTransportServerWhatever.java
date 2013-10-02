package org.reefy.test;

import org.reefy.transportrest.api.transport.Contact;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class AbstractTransportServerWhatever<C extends Contact> implements TransportFactory.ServerWhatever<C> {
    private final C contact;
    private final TransportFactory<C> server;

    public AbstractTransportServerWhatever(C contact, TransportFactory<C> server) {
        this.contact = contact;
        this.server = server;
    }

    @Override
    public C getContact() {
        return contact;
    }

    @Override
    public TransportFactory<C> getServer() {
        return server;
    }
}
