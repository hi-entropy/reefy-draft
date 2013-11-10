package org.reefy.core.transport.local;

import org.reefy.core.transport.test.AbstractTransportStateTest;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class LocalTransportStateTest extends AbstractTransportStateTest<LocalContact> {
    public LocalTransportStateTest() {
        super(new LocalTransportFactory());
    }
}
