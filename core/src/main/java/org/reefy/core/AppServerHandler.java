package org.reefy.core;

import com.google.common.util.concurrent.ListenableFuture;

import org.reefy.core.contract.ContractMessage;
import org.reefy.core.transport.Contact;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public interface AppServerHandler<C extends Contact> {
    public ListenableFuture<PutResponse<C>> put(Key key, Value value);

    public interface PutResponse<C extends Contact> {
        public boolean succeeded();

        //@Nullable
        public C redirected();

        //@Nullable
        public Exception failed();

        //@Nullable
        ContractMessage contract();
    }

    public ListenableFuture<GetResponse<C>> get(Key key);

    public interface GetResponse<C extends Contact> {

        //@Nullable
        public Value succeeded();

        //@Nullable
        public C redirected();

        //@Nullable
        public Exception failed();
    }
}
