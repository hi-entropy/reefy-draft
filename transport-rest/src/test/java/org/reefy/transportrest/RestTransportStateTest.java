package org.reefy.transportrest;

import org.reefy.core.transport.test.AbstractTransportStateTest;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class RestTransportStateTest extends AbstractTransportStateTest<RestContact> {
    public RestTransportStateTest() {
        super(new RestTransportFactory());
    }
}
