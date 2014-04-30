package org.reefy.core;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

import org.reefy.core.contract.ContractMessage;
import org.reefy.core.contract.ContractServer;
import org.reefy.core.store.Store;
import org.reefy.core.store.StoreException;
import org.reefy.core.transport.Contact;
import org.reefy.core.transport.ValueNotFoundException;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * This could be thought of as a non-static inner class of SimpleAppServer.
 *
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class SimpleAppServerHandler<C extends Contact> implements AppServerHandler {
    private final SimpleAppServer<C> owner;
    private final Store store;
    private final ContractServer contractServer;

    public SimpleAppServerHandler(SimpleAppServer<C> owner, Store store,
                                  ContractServer contractServer) {
        this.owner = owner;
        this.store = store;
        this.contractServer = contractServer;
    }

    @Override
    public ListenableFuture<PutResponse<C>> put(Key key, Value value) {
        final SettableFuture<PutResponse<C>> settableFuture = SettableFuture.create();

        // Is there a better guy to store this key?
        final Map.Entry<C, SimpleAppServer.NeighborInfo<C>> bestNeighbor = owner.bestNeighbor(key);
        if (bestNeighbor != null) {
            settableFuture.set(AbstractAppServerHandler.<C>redirectPutResponse(bestNeighbor.getValue().getContact()));
            return settableFuture;
        }

        // Has the client satisfied the contract
        final ContractMessage contractMessage;
        try {
            contractMessage = contractServer.put(key, value).get();
        } catch (InterruptedException | ExecutionException e) {
            settableFuture.set(AbstractAppServerHandler.<C>failPutResponse(e));
            return settableFuture;
        }

        // If the client hasn't satisfied the contract,
        if (contractMessage != null) {
            settableFuture.set(
                AbstractAppServerHandler.<C>contractPutResponse(contractMessage)
            );
        }

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
        final Map.Entry<C, SimpleAppServer.NeighborInfo<C>> bestNeighbor = owner.bestNeighbor(key);

        final SettableFuture<GetResponse<C>> settableFuture = SettableFuture.create();
        if (bestNeighbor != null) {
            settableFuture.set(AbstractAppServerHandler.<C>redirectGetResponse(bestNeighbor.getValue().getContact()));
            return settableFuture;
        }

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
