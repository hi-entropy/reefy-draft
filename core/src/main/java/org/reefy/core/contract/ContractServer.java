package org.reefy.core.contract;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;

import org.reefy.core.Key;
import org.reefy.core.Value;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public interface ContractServer {
    public ListenableFuture<ContractMessage> put(Key client, Key key, Value value);

    //TODO: all of these are nullable
    public ListenableFuture<ContractMessage> applyMessage(ContractMessage message);
}
