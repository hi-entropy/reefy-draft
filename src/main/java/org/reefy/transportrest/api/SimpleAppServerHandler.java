package org.reefy.transportrest.api;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import org.reefy.transportrest.api.store.Store;
import org.reefy.transportrest.api.store.StoreException;
import org.reefy.transportrest.api.transport.Contact;
import org.reefy.transportrest.api.transport.ValueNotFoundException;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class SimpleAppServerHandler<C extends Contact> implements AppServerHandler {
    private final Store store;

    public SimpleAppServerHandler(Store store) {
        this.store = store;
    }

    @Override
    public ListenableFuture<PutResponse<C>> put(Key key, Value value) {
        final SettableFuture<PutResponse<C>> settableFuture = SettableFuture.create();
        store.put(key, value, new Store.PutCallback() {
            @Override
            public void succeed() {
                settableFuture.set(AbstractAppServerHandler.<C>succeedPutResponse());
            }

            @Override
            public void fail(StoreException e) {
                settableFuture.set(AbstractAppServerHandler.<C>failPutResponse(e));
            }
        });

        return settableFuture;
    }

    @Override
    public ListenableFuture<GetResponse<C>> get(Key key) {
        final SettableFuture<GetResponse<C>> settableFuture = SettableFuture.create();
        store.get(key, new Store.GetCallback() {
            @Override
            public void succeed(Value value) {
                settableFuture.set(AbstractAppServerHandler.<C>succeedGetResponse(value));
            }

            @Override
            public void notFound() {
                // TODO: maybe redirect
                settableFuture.set(AbstractAppServerHandler.<C>failGetResponse(new ValueNotFoundException()));
            }

            @Override
            public void fail(StoreException e) {
                settableFuture.set(AbstractAppServerHandler.<C>failGetResponse(e));
            }
        });

        return settableFuture;
    }
}
