package org.reefy.transportrest.api.transport.local;

import com.google.common.util.concurrent.AbstractService;
import com.google.common.util.concurrent.ListenableFuture;
import org.reefy.transportrest.api.*;
import org.reefy.transportrest.api.transport.ContactNotFoundException;
import org.reefy.transportrest.api.transport.TransportClient;
import org.reefy.transportrest.api.transport.TransportException;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class LocalTransportClient extends AbstractService implements TransportClient<LocalContact> {

    // TODO: make this configurable?
    public static final int TIMEOUT = 10000;
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

        final AppServerHandler.PutResponse<LocalContact> putResponse;
        try {
            putResponse = appServerHandler.put(key, value).get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            callback.fail(new TransportException(e));
            return;
        }

        if (putResponse.succeeded()) {
            callback.succeed();
            return;
        }

        if (putResponse.redirected() != null) {
            callback.redirect(putResponse.redirected());
            return;
        }

        if (putResponse.failed() != null) {
            callback.fail(new TransportException(putResponse.failed()));
            return;
        }

        throw new AssertionError("Unreachable case");
    }

    @Override
    public void get(LocalContact contact, Key key, final GetCallback callback) {
        final LocalTransportServer server = contactsToServers.get(contact);

        if (server == null) {
            callback.fail(new ContactNotFoundException());
            return;
        }

        final AppServerHandler<LocalContact> appServerHandler = server.getAppServerHandler();

        final AppServerHandler.GetResponse<LocalContact> getResponse;
        try {
            getResponse = appServerHandler.get(key).get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            callback.fail(new TransportException(e));
            return;
        }

        if (getResponse.succeeded() != null) {
            callback.present(getResponse.succeeded());
            return;
        }

        if (getResponse.redirected() != null) {
            callback.redirect(getResponse.redirected());
            return;
        }

        if (getResponse.failed() != null) {
            callback.fail(new TransportException(getResponse.failed()));
            return;
        }

        throw new AssertionError("Unreachable case");
    }

    @Override
    public void find(LocalContact contact, Key key, FindCallback callback) {
        //TODO: make this
    }
}
