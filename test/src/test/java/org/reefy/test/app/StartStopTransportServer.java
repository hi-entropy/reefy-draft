package org.reefy.test.app;

import org.reefy.transportrest.api.Key;
import org.reefy.transportrest.api.TrivialIdleService;
import org.reefy.transportrest.api.Value;
import org.reefy.transportrest.api.store.Store;
import org.reefy.transportrest.api.store.StoreException;
import org.reefy.transportrest.api.transport.Contact;
import org.reefy.transportrest.api.transport.TransportServer;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class StartStopTransportServer<C extends Contact>
    extends TrivialIdleService implements TransportServer<C> {
}
