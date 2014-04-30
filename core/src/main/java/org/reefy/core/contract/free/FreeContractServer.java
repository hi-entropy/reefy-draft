package org.reefy.core.contract.free;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import org.reefy.core.Key;
import org.reefy.core.Value;
import org.reefy.core.contract.AcceptContractMessage;
import org.reefy.core.contract.ContractMessage;
import org.reefy.core.contract.ContractServer;
import org.reefy.core.contract.RejectContractMessage;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class FreeContractServer implements ContractServer {

    @Override
    public ListenableFuture<ContractMessage> put(Key client, Key key, Value value) {
        return Futures.<ContractMessage>immediateFuture(AcceptContractMessage.get());
    }

    @Override
    public ListenableFuture<ContractMessage> applyMessage(ContractMessage message) {
        return Futures.<ContractMessage>immediateFuture(
            new RejectContractMessage("Unrecognized message type.")
        );
    }
}
