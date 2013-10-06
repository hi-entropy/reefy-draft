package org.reefy.transportrest.api.transport.local;

import com.google.common.util.concurrent.AbstractIdleService;

import org.reefy.transportrest.api.AppServerHandler;
import org.reefy.transportrest.api.TransportServer;

import javax.swing.*;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class LocalTransportServer
    extends AbstractIdleService implements TransportServer<LocalContact> {

    private final AppServerHandler appServerHandler;

    public LocalTransportServer(AppServerHandler appServerHandler) {
        this.appServerHandler = appServerHandler;
    }

    @Override
    protected void startUp() throws Exception {
        // Do nothing
    }

    @Override
    protected void shutDown() throws Exception {
        // Do nothing
    }

    public AppServerHandler getAppServerHandler() {
        return appServerHandler;
    }
}
