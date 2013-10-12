package org.reefy.storesqlite;

import org.reefy.transportrest.api.RawKey;
import org.reefy.transportrest.api.RawValue;
import org.reefy.transportrest.api.Value;

import java.nio.charset.StandardCharsets;
import java.sql.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class SqliteStoreTest {
    public static void main(String[] args) throws ClassNotFoundException {
        // load the sqlite-JDBC driver using the current class loader
        Class.forName("org.sqlite.JDBC");

        Connection connection = null;
        try
        {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
            Statement statement = connection.createStatement();

            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("drop table if exists keys");
            statement.executeUpdate("create table keys (key char(20), value blob)");

            final PreparedStatement preparedStatement = connection.prepareStatement("insert into keys values(?, ?)");
            final RawKey rawKey = RawKey.pseudorandom();
            final Value value = RawValue.pseudorandom(10);
            preparedStatement.setBytes(1, rawKey.getBytes());
            preparedStatement.setBytes(2, value.getBytes());
            preparedStatement.execute();

            final ResultSet rs = statement.executeQuery("select * from keys");
            while(rs.next())
            {
                // read the result set
                final RawKey retrieved = new RawKey(rs.getBytes("key"));
                System.out.println("key = " + retrieved);
                assertThat(retrieved, is(rawKey));
                System.out.println("value = " + new String(rs.getBytes("value"), StandardCharsets.ISO_8859_1));
                assertThat(new RawValue(rs.getBytes("value")), is(value));

            }
        }
        catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        finally
        {
            try
            {
                if(connection != null)
                    connection.close();
            }
            catch(SQLException e)
            {
                // connection close failed.
                System.err.println(e);
            }
        }
    }
}
