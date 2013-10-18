package org.reefy.transportrest;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class ReefyServletModule extends ServletModule {

    @Override
    protected void configureServlets() {
        bind(KeyResource.class);

        // hook Jersey into Guice Servlet
        bind(GuiceContainer.class);

        // hook Jackson into Jersey as the POJO <-> JSON mapper
        bind(JacksonJsonProvider.class).in(Scopes.SINGLETON);

        serve("/*").with(GuiceContainer.class);
    }
}
