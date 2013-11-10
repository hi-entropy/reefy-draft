package org.reefy.core;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.AbstractIdleService;

import org.reefy.core.store.Store;
import org.reefy.core.store.StoreException;
import org.reefy.core.transport.Contact;
import org.reefy.core.transport.TransportServer;
import org.reefy.core.transport.TransportServerFactory;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class SimpleAppServer<C extends Contact> extends AbstractIdleService {
    private final Store store;
    private final AppServerHandler handler;
    private final TransportServer transportServer;
    private final ConcurrentMap<C, NeighborInfo<C>> neighbors = Maps.newConcurrentMap();

    public static class NeighborInfo<C extends Contact> {
        private final C contact;

        private NeighborInfo(C contact) {
            this.contact = contact;
        }

        public C getContact() {
            return contact;
        }
    }

    public SimpleAppServer(Store store, TransportServerFactory transportServer) {
        this.store = store;
        this.handler = new SimpleAppServerHandler(this, store);
        this.transportServer = transportServer.build(this.handler);
    }

    @Override
    protected void startUp() throws Exception {
        store.start().get();
        transportServer.start().get();
    }

    @Override
    protected void shutDown() throws Exception {
        store.stop().get();
        transportServer.stop().get();
    }

    public void clear() throws StoreException {
        this.store.clear();
    }

    public C getContact() {
        return (C) transportServer.getContact();
    }

    public void addNeighbor(C contact) {
        this.neighbors.put(contact, new NeighborInfo(contact));
    }

    public ConcurrentMap<C, NeighborInfo<C>> getNeighbors() {
        return neighbors;
    }

    //@Nullable
    public Map.Entry<C, NeighborInfo<C>> bestNeighbor(Key key) {
        final BigInteger myDistance = getContact().getKey().distance(key);

        Map.Entry<C, NeighborInfo<C>> bestNeighbor = null;
        BigInteger bestNeighborKeyDistance = null;

        for (Map.Entry<C, SimpleAppServer.NeighborInfo<C>> neighbor : neighbors.entrySet()) {
            final BigInteger thisNeighborKeyDistance = neighbor.getKey().getKey().distance(key);
            final boolean betterThanNeighbors = bestNeighbor == null || thisNeighborKeyDistance.compareTo(bestNeighborKeyDistance) == -1;
            final boolean betterThanMe = thisNeighborKeyDistance.compareTo(myDistance) == -1;
            if (betterThanNeighbors && betterThanMe) {

                bestNeighbor = neighbor;
                bestNeighborKeyDistance = thisNeighborKeyDistance;
            }
        }

        return bestNeighbor;
    }
}
