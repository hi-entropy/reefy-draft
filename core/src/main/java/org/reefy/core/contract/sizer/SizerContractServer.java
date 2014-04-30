package org.reefy.core.contract.sizer;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import org.reefy.core.Key;
import org.reefy.core.contract.transaction.AbstractContractServer;
import org.reefy.core.contract.transaction.AbstractContractTransaction;
import org.reefy.core.contract.AcceptContractMessage;
import org.reefy.core.contract.ContractMessage;
import org.reefy.core.contract.transaction.ContractTransaction;
import org.reefy.core.contract.RejectContractMessage;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class SizerContractServer extends AbstractContractServer {

    public class PutTransaction extends AbstractContractTransaction {

        // This is -1 until a size message has been received
        private int size = -1;

        public PutTransaction(Key counterparty) {
            super(counterparty);
        }

        @Override
        public ListenableFuture<ContractMessage> nextMessage() {

            // If we haven't yet received a size message
            if (size < 0) {
                return Futures.immediateFuture((ContractMessage) new RejectContractMessage(
                    "No size message has been received."
                ));
            }

            // If the server decides that this value is too large
            if (size > maxSize) {
                return Futures.immediateFuture((ContractMessage) new RejectContractMessage(
                    "This value is too large."
                ));
            }

            // TODO: ask for store capacity?

            // We're good to go
            return Futures.immediateFuture((ContractMessage) AcceptContractMessage.get());
        }
    }

    private final int maxSize;

    public SizerContractServer(int maxSize) {
        super(Maps.<String, MessageHandler>newConcurrentMap());
        this.maxSize = maxSize;
    }

    @Override
    public ContractTransaction newPutTransaction(Key client) {
        return new PutTransaction(client);
    }
}
