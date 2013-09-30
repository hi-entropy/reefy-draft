package org.reefy.transportrest.api;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.AbstractFuture;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

import org.reefy.transportrest.api.store.Store;
import org.reefy.transportrest.api.store.StoreException;
import org.reefy.transportrest.api.transport.Contact;

import java.nio.ByteBuffer;
import java.util.Comparator;
import java.util.Map;
import java.util.Random;

/**
 * Immutable
 *
 * All constructors make defensive copies.
 *
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class RawKey implements Key {

    public static Comparator<Contact> distanceComparator(final Key key) {
        return new Comparator<Contact>() {
            @Override
            public int compare(Contact contact1,
                               Contact contact2) {
                return contact1.getKey().distance(key) -
                       contact2.getKey().distance(key);
            }
        };
    }

    public static RawKey pseudorandom() {
        final byte[] data = new byte[20];
        new Random().nextBytes(data);
        return new RawKey(data);
    }

    public final ByteBuffer data;

    public RawKey(ByteBuffer data) {
        Preconditions.checkArgument(data.limit() == 20);
        this.data = ByteBuffer.allocate(20);
        this.data.put(data);
    }

    public RawKey(byte[] data) {
        this(ByteBuffer.allocate(20).put(data));
    }

    @Override
    public ByteBuffer getBytes() {
        return data;
    }

    // Hamming/XOR/Manhattan distance
    @Override
    public int distance(Key otherKey) {
        final int[] myInts = data.asIntBuffer().array();
        final int[] otherInts = otherKey.getBytes().asIntBuffer().array();

        int distance = 0;
        for (int i = 0; i < 5; i++) {
            distance += Integer.bitCount(myInts[i] ^ otherInts[i]);
        }
        return distance;
    }

    //    @Override
//    public ListenableFuture get(Store store) {
//        final SettableFuture future = SettableFuture.create();
//        store.get(this, new Store.GetCallback() {
//            @Override
//            public void succeed(Value<V> value) {
//                future.set(value.get());
//            }
//
//            @Override
//            public void fail(StoreException error) {
//                future.setException(error);
//            }
//        });
//        return future;
//    }
}
