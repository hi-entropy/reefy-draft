package org.reefy.core.app;

import org.reefy.core.TrivialIdleService;
import org.reefy.core.transport.Contact;
import org.reefy.core.transport.TransportServer;

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
