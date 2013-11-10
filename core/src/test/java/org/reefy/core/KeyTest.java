package org.reefy.core;

import org.junit.Test;

import java.math.BigInteger;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class KeyTest {

    @Test
    public void testEquality() {
        final RawKey rawKey1 = RawKey.pseudorandom();
        final RawKey rawKey2 = RawKey.pseudorandom();

        // Probability of equality is infinitesimal
        assertThat(rawKey1, not(rawKey2));

        final RawKey copiedRawKey = RawKey.copy(rawKey1);

        assertThat(copiedRawKey, is(rawKey1));
    }

    @Test
    public void testDistance() {
        // @ is 01000000 in ASCII
        // A is 01000001
        final RawKey rawKey1 = RawKey.from("@@@@@@@@@@@@@@@@@@@@".getBytes());
        final RawKey rawKey2 = RawKey.from("@@@@@@@@@@@@@@@@@@@A".getBytes());
        final RawKey rawKey3 = RawKey.from("@@@@@@@@@@@@@@@@@@A@".getBytes());

        assertThat(rawKey1.distance(rawKey1), is(BigInteger.valueOf(0)));
        assertThat(rawKey1.distance(rawKey2), is(BigInteger.valueOf(1)));
        assertThat(rawKey1.distance(rawKey3), is(BigInteger.valueOf(0b100000000L)));

        assertThat(rawKey2.distance(rawKey2), is(BigInteger.valueOf(0)));
        assertThat(rawKey2.distance(rawKey3), is(BigInteger.valueOf(0b100000001L)));

        assertThat(rawKey3.distance(rawKey3), is(BigInteger.valueOf(0)));
    }
}
