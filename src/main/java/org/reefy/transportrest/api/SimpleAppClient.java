package org.reefy.transportrest.api;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AbstractIdleService;

import org.joda.time.Duration;
import org.reefy.transportrest.api.transport.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class SimpleAppClient extends AbstractIdleService implements AppClient {

    private static final int N_PATHS = 3;
    private static final int MAX_N_HOPS = 32;

    private final TransportClient transport;
    private final List<Contact> contacts = Lists.newArrayList();

    public SimpleAppClient(TransportClient transport) {
        this.transport = transport;
    }

    @Override
    public void get(final Key key, final GetCallback callback) {
        if (contacts.isEmpty()) {
            callback.fail(new NoContactsException());
        }

        final ArrayList<Contact> contactsSorted = new ArrayList<Contact>(contacts);
        Collections.sort(contactsSorted, RawKey.distanceComparator(key));

        // This is sketchy...
        final TransportClient.GetCallback getter = new TransportClient.GetCallback() {
            volatile int nTimesCalled = 0;

            @Override
            public void succeed(Value value) {
                callback.succeed(value);
            }

            // Keep trying until we find the right server
            @Override
            public void redirect(Contact contact) {
                ++nTimesCalled;
                if (nTimesCalled <= MAX_N_HOPS) {
                    transport.get(contact, key, this);
                } else {
                    callback.fail(new HopsExceededException());
                }
            }

            @Override
            public void fail(TransportException exception) {
                callback.fail(exception);
            }
        };

        getter.redirect(contactsSorted.get(0));
    }

    @Override
    public void put(final Key key, final Value value, final Duration duration, final PutCallback callback) {
        if (contacts.isEmpty()) {
            callback.fail(new NoContactsException());
            return;
        }

        final ArrayList<Contact> contactsSorted = new ArrayList<Contact>(contacts);
        Collections.sort(contactsSorted, RawKey.distanceComparator(key));

        // This is sketchy...
        final TransportClient.PutCallback putter = new TransportClient.PutCallback() {
            volatile int nTimesCalled = 0;

            @Override
            public void succeed() {
                callback.succeed();
            }

            // Keep trying until we find the right server
            @Override
            public void redirect(Contact contact) {
                ++nTimesCalled;
                if (nTimesCalled <= MAX_N_HOPS) {
                    transport.put(contact, key, value, this);
                } else {
                    callback.fail(new HopsExceededException());
                }
            }

            @Override
            public void fail(TransportException exception) {
                callback.fail(exception);
            }
        };

        putter.redirect(contactsSorted.get(0));
    }

    @Override
    protected void startUp() throws Exception {
        transport.startAndWait();
    }

    @Override
    protected void shutDown() throws Exception {
        transport.stopAndWait();
    }
}
