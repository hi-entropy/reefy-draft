package org.reefy.test;

import org.reefy.transportrest.api.transport.local.LocalContact;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class LocalTransportTest extends AbstractTransportTest<LocalContact> {

    public LocalTransportTest() {
        super(new LocalTransportFactory());
    }
}
