package org.reefy.test.transport.local;

import org.reefy.test.transport.AbstractTransportSingleTest;
import org.reefy.transportrest.api.transport.local.LocalContact;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class LocalTransportSingleTest extends AbstractTransportSingleTest<LocalContact> {

    public LocalTransportSingleTest() {
        super(new LocalTransportFactory());
    }
}
