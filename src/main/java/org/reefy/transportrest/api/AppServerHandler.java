package org.reefy.transportrest.api;

import org.reefy.transportrest.api.transport.Contact;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public interface AppServerHandler<C extends Contact> {
    public void get(Key key, GetCallback<C> callback);

    public interface GetCallback<C extends Contact> {
        public void present(Value value);

        public void redirect(C contact);

        public void notFound();

        public void fail(Exception e);
    }
}
