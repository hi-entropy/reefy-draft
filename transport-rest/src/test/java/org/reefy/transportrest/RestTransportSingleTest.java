package org.reefy.transportrest;

import org.reefy.test.transport.AbstractTransportSingleTest;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class RestTransportSingleTest extends AbstractTransportSingleTest<RestContact> {
    public RestTransportSingleTest() {
        super(new RestTransportFactory());
    }
}
