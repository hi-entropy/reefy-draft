package org.reefy.test.transport.local;

import org.reefy.test.transport.AbstractTransportStateTest;
import org.reefy.transportrest.api.transport.local.LocalContact;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class LocalTransportStateTest extends AbstractTransportStateTest<LocalContact> {
    public LocalTransportStateTest() {
        super(new LocalTransportFactory());
    }
}
