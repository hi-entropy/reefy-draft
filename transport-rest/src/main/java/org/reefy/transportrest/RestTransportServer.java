package org.reefy.transportrest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.AbstractService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;

import org.eclipse.jetty.server.DispatcherType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.reefy.transportrest.api.AppServerHandler;
import org.reefy.transportrest.api.TransportServer;

import java.util.EnumSet;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class RestTransportServer extends AbstractService implements TransportServer<RestContact> {

    private final RestContact restContact;

    private final Server server;

    public RestTransportServer(RestContact restContact, AppServerHandler appServerHandler) {
        this.restContact = restContact;

        final Injector injector = Guice.createInjector();

        server = new Server(restContact.getPort());
        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.setContextPath("/");

        servletContextHandler.addServlet(new ServletHolder(new RestTransportServlet(appServerHandler)), "/*");

        FilterHolder guiceFilter = new FilterHolder(injector.getInstance(GuiceFilter.class));
        servletContextHandler.addFilter(guiceFilter, "/*", EnumSet.allOf(DispatcherType.class));

        server.setHandler(servletContextHandler);
    }


    @Override
    protected void doStart() {
        try {
            server.start();
        } catch (Exception e) {
            notifyFailed(e);
        }
        notifyStarted();
    }

    @Override
    protected void doStop() {
        try {
            server.stop();
        } catch (Exception e) {
            notifyFailed(e);
        }
        notifyStopped();
    }
}
