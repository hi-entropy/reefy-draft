package org.reefy.storesqlite;

import org.reefy.test.store.AbstractStoreTest;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class SqliteStoreTest extends AbstractStoreTest {

    public SqliteStoreTest() {
        super(new SqliteStoreFactory());
    }
}
