package org.reefy.transportrest.api;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public interface AppServerHandler {
    public void get(Key key, GetCallback callback);

    public interface GetCallback {
        public void present(Value value);

        public void notFound();

        public void fail(Exception e);
    }
}
