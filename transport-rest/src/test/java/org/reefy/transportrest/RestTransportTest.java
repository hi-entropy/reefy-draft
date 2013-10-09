package org.reefy.transportrest;

import org.reefy.test.transport.AbstractTransportTest;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class RestTransportTest extends AbstractTransportTest<RestContact> {
    public RestTransportTest() {
        super(new RestTransportFactory());
    }
}
