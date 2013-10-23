package org.reefy.transportrest.api.transport;

import com.google.common.util.concurrent.Service;

import org.reefy.transportrest.api.transport.Contact;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public interface TransportServer<C extends Contact> extends Service {
    public C getContact();
}
