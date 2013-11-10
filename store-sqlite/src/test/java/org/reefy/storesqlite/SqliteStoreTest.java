package org.reefy.storesqlite;

import org.reefy.core.store.test.AbstractStoreTest;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class SqliteStoreTest extends AbstractStoreTest {

    public static final int TIMEOUT_MILLIS = 1000;

    public SqliteStoreTest() {
        super(new SqliteStoreFactory(), TIMEOUT_MILLIS);
    }
}
