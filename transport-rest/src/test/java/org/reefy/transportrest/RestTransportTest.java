package org.reefy.transportrest;

import org.reefy.test.AbstractTransportClientTest;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class RestTransportTest extends AbstractTransportClientTest<RestContact> {
    public RestTransportTest() {
        super(new RestTransportFactory());
    }
}
