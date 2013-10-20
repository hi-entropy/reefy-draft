package org.reefy.test.app;

import org.reefy.transportrest.api.AppClient;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public interface AppFactory {
    public AppClient buildClient();
}
