package org.reefy.transportrest;

import org.reefy.test.AbstractIntegrationTest;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class RestTransportTest extends AbstractIntegrationTest<RestContact> {
    public RestTransportTest() {
        super(new RestTransportFactory(), appFactory);
    }
}
