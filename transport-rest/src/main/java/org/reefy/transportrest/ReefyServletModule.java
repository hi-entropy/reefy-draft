package org.reefy.transportrest;

import com.google.inject.servlet.ServletModule;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class ReefyServletModule extends ServletModule {

    @Override
    protected void configureServlets() {
        serve("*.html").with(RestTransportServlet.class);
    }
}
