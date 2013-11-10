package org.reefy.transportrest;

import com.google.common.util.concurrent.AbstractService;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.servlet.GuiceFilter;

import org.eclipse.jetty.server.DispatcherType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.reefy.core.AppServerHandler;
import org.reefy.core.transport.TransportServer;

import java.util.EnumSet;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class RestTransportServer extends AbstractService implements TransportServer<RestContact> {

    private final RestContact contact;

    private final Server server;

    public RestTransportServer(RestContact contact, final AppServerHandler appServerHandler) {
        this.contact = contact;

        server = new Server(contact.getPort());
        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.setContextPath("/");

        //servletContextHandler.addServlet(new ServletHolder(new RestTransportServlet(appServerHandler)), "/*");
        servletContextHandler.addServlet(new ServletHolder(new NotFoundServlet()), "/*");

        final Injector injector = Guice.createInjector(new ReefyServletModule(), new AbstractModule() {
            @Override
            protected void configure() {
                binder().requireExplicitBindings();
                bind(GuiceFilter.class);
                bind(new TypeLiteral<AppServerHandler<RestContact>>() {}).toInstance(appServerHandler);
            }
        });

        final FilterHolder guiceFilter = new FilterHolder(injector.getInstance(GuiceFilter.class));
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

    @Override
    public RestContact getContact() {
        return contact;
    }
}
