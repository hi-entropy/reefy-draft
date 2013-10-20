package org.reefy.transportrest;

import org.reefy.test.transport.AbstractTransportStateTest;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class RestTransportStateTest extends AbstractTransportStateTest<RestContact> {
    public RestTransportStateTest() {
        super(new RestTransportFactory());
    }
}
