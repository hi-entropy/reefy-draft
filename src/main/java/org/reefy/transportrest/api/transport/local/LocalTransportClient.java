package org.reefy.transportrest.api.transport.local;

import com.google.common.util.concurrent.AbstractService;
import org.reefy.transportrest.api.*;
import org.reefy.transportrest.api.transport.ContactNotFoundException;
import org.reefy.transportrest.api.transport.TransportClient;
import org.reefy.transportrest.api.transport.WhateverException;

import java.util.concurrent.ConcurrentMap;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class LocalTransportClient extends AbstractService implements TransportClient<LocalContact> {

    private final ConcurrentMap<LocalContact, AppServer> contactsToServers;

    public LocalTransportClient(ConcurrentMap<LocalContact, AppServer> contactsToServers) {
        this.contactsToServers = contactsToServers;
    }

    @Override
    protected void doStart() {
        // No startup routine is required
    }

    @Override
    protected void doStop() {
        // Nothing to clean up
    }

    @Override
    public void get(LocalContact contact, Key key, final GetCallback callback) {
        final AppServer server = contactsToServers.get(contact);

        if (server == null) {
            callback.fail(new ContactNotFoundException());
            return;
        }

        server.get(key, new AppServer.GetCallback() {
            @Override
            public void succeed(Value value) {
                callback.succeed(value);
            }

            @Override
            public void fail(Exception e) {
                callback.fail(new WhateverException());
            }
        });
    }

    @Override
    public void put(LocalContact contact, Key key, Value value, PutCallback callback) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void find(LocalContact contact, Key key, FindCallback callback) {
        //TODO: make this
    }
}
