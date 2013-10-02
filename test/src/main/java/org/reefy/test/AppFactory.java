package org.reefy.test;

import org.reefy.transportrest.api.AppClient;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public interface AppFactory {
    public AppClient buildClient();
}
