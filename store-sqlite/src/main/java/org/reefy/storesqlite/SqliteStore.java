package org.reefy.storesqlite;

import org.reefy.transportrest.api.Key;
import org.reefy.transportrest.api.RawKey;
import org.reefy.transportrest.api.RawValue;
import org.reefy.transportrest.api.Value;
import org.reefy.transportrest.api.store.Store;
import org.reefy.transportrest.api.store.StoreException;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class SqliteStore implements Store {

    public volatile Connection connection = null;

    // TODO: This might be bad for perf
    private synchronized void initialize() throws StoreException {
        // create a database connection
        if (connection == null) {
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                throw new AssertionError("JDBC driver not found. This should probably never happen.");
            }

            new File("db").mkdirs();

            try {
                connection = DriverManager.getConnection("jdbc:sqlite:db/sample.db");
            } catch (SQLException e) {
                throw new StoreException(e);
            }
        }
    }

    @Override
    public synchronized void clear() throws StoreException {

        initialize();

        try
        {
            final Statement statement = connection.createStatement();

            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("drop table if exists keys");
            statement.executeUpdate("create table keys (key char(20), value blob)");
            statement.close();
        } catch (SQLException e) {
            throw new StoreException(e);
        }
    }

    @Override
    public synchronized <V> void put(Key key, Value<V> value, PutCallback<V> callback) {
        try {
            initialize();
        } catch (StoreException e) {
            callback.fail(e);
            return;
        }

        try {
            final PreparedStatement putStatement = connection.prepareStatement("insert into keys values(?, ?)");
            putStatement.setBytes(1, key.getBytes());
            putStatement.setBytes(2, value.getBytes());
            putStatement.execute();
            putStatement.close();
        } catch (SQLException e) {
            callback.fail(new StoreException(e));
        }
        callback.succeed();
    }

    @Override
    public synchronized void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            // Swallow this probably
            // TODO: log
        }
    }

    @Override
    public synchronized <V> void get(Key key, GetCallback<V> callback) {
        try {
            initialize();
        } catch (StoreException e) {
            callback.fail(e);
            return;
        }

        try {
            Statement statement = connection.createStatement();

            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            final ResultSet rs = statement.executeQuery("select * from keys");

            if (!rs.next()) {
                callback.notFound();
                return;
            }

            // read the result set
            final RawKey retrieved = new RawKey(rs.getBytes("key"));
            System.out.println("key = " + retrieved);
            System.out.println("value = " + new String(rs.getBytes("value"), StandardCharsets.ISO_8859_1));

            callback.succeed(new RawValue(rs.getBytes("value")));
        } catch (SQLException e) {
            callback.fail(new StoreException(e));
        }
    }
}