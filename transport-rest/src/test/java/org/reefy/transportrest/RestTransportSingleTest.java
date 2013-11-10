package org.reefy.transportrest;

import org.reefy.core.transport.test.AbstractTransportSingleTest;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class RestTransportSingleTest extends AbstractTransportSingleTest<RestContact> {
    public RestTransportSingleTest() {
        super(new RestTransportFactory());
    }
}
