package org.reefy.transportrest.api;

import com.google.common.util.concurrent.ListenableFuture;
import org.reefy.transportrest.api.transport.Contact;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public interface AppServerHandler<C extends Contact> {
    public ListenableFuture<PutResponse<C>> put(Key key, Value value);

    public interface PutResponse<C extends Contact> {
        public boolean succeeded();

        public C redirected();

        public Exception failed();
    }

    public ListenableFuture<GetResponse<C>> get(Key key);

    public interface GetResponse<C extends Contact> {
        public Value succeeded();

        public C redirected();

        public Exception failed();
    }
}
