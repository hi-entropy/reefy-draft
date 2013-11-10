package org.reefy.core.transport.local;

import org.reefy.core.transport.test.AbstractTransportSingleTest;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class LocalTransportSingleTest extends AbstractTransportSingleTest<LocalContact> {

    public LocalTransportSingleTest() {
        super(new LocalTransportFactory());
    }
}
