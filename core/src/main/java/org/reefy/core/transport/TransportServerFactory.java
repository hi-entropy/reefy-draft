package org.reefy.core.transport;

import org.reefy.core.AppServerHandler;

/**
 * TODO: make this a superclass of TransportFactory
 *
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public interface TransportServerFactory<C extends Contact> {
    public TransportServer<C> build(AppServerHandler handler);
}
