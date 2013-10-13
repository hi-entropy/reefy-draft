package org.reefy.transportrest.api.transport.local;

import com.google.common.util.concurrent.AbstractService;
import org.reefy.transportrest.api.*;
import org.reefy.transportrest.api.transport.ContactNotFoundException;
import org.reefy.transportrest.api.transport.TransportClient;
import org.reefy.transportrest.api.transport.TransportException;

import java.util.concurrent.ConcurrentMap;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class LocalTransportClient extends AbstractService implements TransportClient<LocalContact> {

    private final ConcurrentMap<LocalContact, LocalTransportServer> contactsToServers;

    public LocalTransportClient(
        ConcurrentMap<LocalContact, LocalTransportServer> contactsToServers) {
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
    public void put(LocalContact contact, Key key, Value value, final PutCallback callback) {
        final LocalTransportServer server = contactsToServers.get(contact);

        if (server == null) {
            callback.fail(new ContactNotFoundException());
            return;
        }

        final AppServerHandler<LocalContact> appServerHandler = server.getAppServerHandler();
        appServerHandler.put(key, value, new AppServerHandler.PutCallback<LocalContact>() {
            @Override
            public void succeed() {
                callback.succeed();
            }

            @Override
            public void redirect(LocalContact contact) {
                callback.redirect(contact);
            }


            @Override
            public void fail(Exception e) {
                callback.fail(new TransportException(e));
            }
        });
    }

    @Override
    public void get(LocalContact contact, Key key, final GetCallback<LocalContact> callback) {
        final LocalTransportServer server = contactsToServers.get(contact);

        if (server == null) {
            callback.fail(new ContactNotFoundException());
            return;
        }

        final AppServerHandler<LocalContact> appServerHandler = server.getAppServerHandler();
        appServerHandler.get(key, new AppServerHandler.GetCallback<LocalContact>() {
            @Override
            public void present(Value value) {
                callback.present(value);
            }

            @Override
            public void notFound() {
                callback.notFound();
            }

            @Override
            public void redirect(LocalContact contact) {
                callback.redirect(contact);
            }


            @Override
            public void fail(Exception e) {
                callback.fail(new TransportException(e));
            }
        });
    }

    @Override
    public void find(LocalContact contact, Key key, FindCallback callback) {
        //TODO: make this
    }
}
