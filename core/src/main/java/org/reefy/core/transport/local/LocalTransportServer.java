package org.reefy.core.transport.local;

import com.google.common.util.concurrent.AbstractIdleService;

import org.reefy.core.AppServerHandler;
import org.reefy.core.TrivialIdleService;
import org.reefy.core.transport.TransportServer;
import org.reefy.core.transport.Contact;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class LocalTransportServer
    extends TrivialIdleService implements TransportServer<LocalContact> {

    private final LocalContact contact;
    private final AppServerHandler<?> appServerHandler;

    public LocalTransportServer(LocalContact contact, AppServerHandler appServerHandler) {
        this.contact = contact;
        this.appServerHandler = appServerHandler;
    }

    // TODO: This is probably not the best place to do this cast.
    public <C extends Contact> AppServerHandler<C> getAppServerHandler() {
        return (AppServerHandler<C>) appServerHandler;
    }

    @Override
    public LocalContact getContact() {
        return contact;
    }
}
