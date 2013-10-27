package org.reefy.transportrest.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.google.common.base.Preconditions;
import org.reefy.transportrest.api.transport.Contact;

import java.math.BigInteger;
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
            public int compare(Contact contact1, Contact contact2) {
                return contact1.getKey().distance(key).compareTo(contact2.getKey().distance(key));
            }
        };
    }

    /**
     * This lets users create keys from custom strings of bytes. Use nonrandom keys at your own risk.
     *
     * @return
     */
    public static RawKey from(byte[] bytes) {
        return new RawKey(bytes);
    }

    public static RawKey pseudorandom() {
        final byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        return new RawKey(bytes);
    }

    public static RawKey copy(Key other) {
        return new RawKey(other.getBytes());
    }

    // This is never changed. We use copying to prevent this value from being exposed to end users.
    private final byte[] bytes;

    /**
     * Make a defensive copy; the key is NOT backed by the input, it's copied. This is private because making one's own
     * keys could lead to keys being distributed differently. The {@link #from} method can be used for this.
     *
     * @param bytes
     */
    private RawKey(@JsonProperty("bytes") byte[] bytes) {
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
    public BigInteger distance(Key otherKey) {
        return new BigInteger(bytes).xor(new BigInteger(otherKey.getBytes()));
    }

    // Still hashing this because it might be legitimate to have non-uniform random key distributions. Can't think
    // why...
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
