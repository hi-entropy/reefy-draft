package org.reefy.core.contract.transaction;

import com.google.common.util.concurrent.ListenableFuture;

import org.reefy.core.Key;
import org.reefy.core.Value;
import org.reefy.core.contract.ContractClient;
import org.reefy.core.contract.ContractMessage;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public abstract class AbstractContractClient implements ContractClient {



    @Override
    public ListenableFuture<ContractMessage> put(Key server, Key key, Value value) {

    }

    @Override
    public ListenableFuture<ContractMessage> applyMessage(ContractMessage message) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
