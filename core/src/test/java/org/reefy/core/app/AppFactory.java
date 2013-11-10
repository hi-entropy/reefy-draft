package org.reefy.core.app;

import org.reefy.core.AppClient;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public interface AppFactory {
    public AppClient buildClient();
}
