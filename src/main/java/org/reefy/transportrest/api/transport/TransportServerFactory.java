package org.reefy.transportrest.api.transport;

import org.reefy.transportrest.api.AppServerHandler;

/**
 * TODO: make this a superclass of TransportFactory
 *
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public interface TransportServerFactory<C extends Contact> {
    public TransportServer<C> build(AppServerHandler handler);
}
