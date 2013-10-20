package org.reefy.test.transport.local;

import org.junit.Test;
import org.reefy.transportrest.api.RawKey;
import org.reefy.transportrest.api.transport.local.LocalContact;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class LocalContactTest {

    @Test
    public void testEquality() {
        final RawKey key1 = RawKey.pseudorandom();
        final LocalContact contact1 = new LocalContact(key1);
        final LocalContact contact2 = new LocalContact(RawKey.pseudorandom());
        final LocalContact contact3 = new LocalContact(key1);

        assertThat(contact1, is(contact1));
        assertThat(contact1, is(contact3));
        assertThat(contact3, is(contact1));

        // Probability of equality is infinitesimal
        assertThat(contact1, not(contact2));
        assertThat(contact2, not(contact1));
    }
}
