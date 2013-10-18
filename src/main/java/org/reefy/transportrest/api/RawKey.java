package org.reefy.transportrest.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.google.common.base.Preconditions;
import org.reefy.transportrest.api.transport.Contact;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import static javax.xml.bind.DatatypeConverter.printHexBinary;

/**
 * Immutable
 *
 * All constructors make defensive copies.
 *
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class RawKey implements Key {

    private static final Random random = new Random();

    /**
     * Orders distances from smallest to largest.
     * @param key
     * @return
     */
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
        final byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        return new RawKey(bytes);
    }

    // TODO: this should be immutable.
    public final byte[] bytes;

    /**
     * Make a defensive copy; the key is NOT backed by the input, it's copied.
     * @param bytes
     */
    public RawKey(@JsonProperty("bytes") byte[] bytes) {
        Preconditions.checkNotNull(bytes);
        Preconditions.checkArgument(bytes.length == 20, "Incoming array length should be 20, not " + bytes.length);
        this.bytes = Arrays.copyOf(bytes, 20);
    }

    /**
     * @return a defensive copy. You never know these days.
     */
    @Override
    public byte[] getBytes() {
        return Arrays.copyOf(bytes, 20);
    }

    // Hamming/XOR/Manhattan distance
    @Override
    public int distance(Key otherKey) {
        final int[] myInts = ByteBuffer.wrap(bytes).asIntBuffer().array();
        final int[] otherInts = ByteBuffer.wrap(otherKey.getBytes()).asIntBuffer().array();

        int distance = 0;
        for (int i = 0; i < 5; i++) {
            distance += Integer.bitCount(myInts[i] ^ otherInts[i]);
        }
        return distance;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (! (obj instanceof Key)) { return false; }

        return Arrays.equals(bytes, ((Key) obj).getBytes());
    }

    @Override
    public String toString() {
        return printHexBinary(bytes);
    }
}
