package org.reefy.transportrest.api;

import com.google.common.util.concurrent.ListenableFuture;

import org.reefy.transportrest.api.store.Store;

import java.nio.ByteBuffer;

/**
 * This represents a piece of data that can be stored on a single machine.
 *
 * @author Paul Kernfeld - pk@knewton.com
 */
public interface Key {
    public byte[] getBytes();

//    public ListenableFuture<V> get(Store store);
//
//    public void put(Store store, Value<V> testValue);

    public int distance(Key key);
}
