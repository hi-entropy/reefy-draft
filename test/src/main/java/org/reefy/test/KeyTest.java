package org.reefy.test;

import org.junit.Test;
import org.reefy.transportrest.api.RawKey;

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
        assertThat(rawKey1, is(not(rawKey2)));

        final RawKey copiedRawKey = new RawKey(rawKey1.getBytes());

        assertThat(copiedRawKey, is(rawKey1));
    }
}
