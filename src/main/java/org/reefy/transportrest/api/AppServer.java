package org.reefy.transportrest.api;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public interface AppServer {
    public void get(Key key, GetCallback callback);

    public interface GetCallback {
        public void succeed(Value value);

        public void fail(Exception e);
    }
}
