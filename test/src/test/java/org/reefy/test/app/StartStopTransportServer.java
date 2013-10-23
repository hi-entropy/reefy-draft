package org.reefy.test.app;

import org.reefy.transportrest.api.TrivialIdleService;
import org.reefy.transportrest.api.transport.Contact;
import org.reefy.transportrest.api.transport.TransportServer;

/**
 * For testing services.
 *
 * @author Paul Kernfeld - pk@knewton.com
 */
public class StartStopTransportServer<C extends Contact>
    extends TrivialIdleService implements TransportServer<C> {

    @Override
    public C getContact() {
        throw new UnsupportedOperationException("This server only starts and stops.");
    }
}
