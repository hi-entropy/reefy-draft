package org.reefy.core.contract;

import com.google.common.util.concurrent.ListenableFuture;

import org.reefy.core.Key;
import org.reefy.core.Value;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public interface ContractClient {

    public ListenableFuture<ContractMessage> put(Key server, Key key, Value value);

    public ListenableFuture<ContractMessage> applyMessage(ContractMessage message);

}
