package org.reefy.core.contract.transaction;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import org.apache.commons.lang3.tuple.Pair;
import org.reefy.core.Key;
import org.reefy.core.Value;
import org.reefy.core.contract.ContractMessage;
import org.reefy.core.contract.ContractServer;
import org.reefy.core.contract.RejectContractMessage;
import org.reefy.core.contract.sizer.SizerContractServer;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public abstract class AbstractContractServer implements ContractServer {

    public static abstract class MessageHandler<M extends ContractMessage> {
        public abstract ListenableFuture<ContractMessage> handle(M message);
    }

//    public static class MessageHandlers {
//        private final Map<String, MessageHandler> messageHandlers;
//
//        private MessageHandlers(Map<String, MessageHandler> messageHandlers) {
//            this.messageHandlers = messageHandlers;
//        }
//
//        public Map<String, MessageHandler> getMessageHandlers() {
//            return messageHandlers;
//        }
//    }
//
//    public static class MessageHandlersBuilder {
//
//        private final Map<String, MessageHandler> messageHandlers = Maps.newHashMap();
//
//        // TODO: make this stronger
//        public void add(String messageType, MessageHandler handler) {
//            messageHandlers.put(messageType, handler);
//        }
//
//        public MessageHandlers build() {
//            return new MessageHandlers(messageHandlers);
//        }
//    }

    private final TransactionMap<SizerContractServer.PutTransaction> putTransactions =
        new TransactionMap<SizerContractServer.PutTransaction>() {
            @Override
            public SizerContractServer.PutTransaction newTransaction() {
                return new SizerContractServer.PutTransaction()
            }
        };

    private final Map<String, MessageHandler> messageHandlers;

    protected AbstractContractServer(Map<String, MessageHandler> messageHandlers) {
        this.messageHandlers = messageHandlers;
    }

    @Override
    public final ListenableFuture<ContractMessage> put(Key client, Key key, Value value) {
        final ContractTransaction newTransaction = newPutTransaction(client);
        final ContractTransaction oldTransaction =
            putTransactions.putIfAbsent(Pair.of(client, key), newTransaction);
        if (oldTransaction != null) {
            return oldTransaction.nextMessage();
        }
        return newTransaction.nextMessage();
    }

    @Override
    public final ListenableFuture<ContractMessage> applyMessage(ContractMessage message) {
        if (messageHandlers.containsKey(message.getType())) {
            // TODO: check that the messageHandlers map is constructed properly
            return messageHandlers.get(message.getType()).handle(message);
        }

        return Futures.<ContractMessage>immediateFuture(
            new RejectContractMessage("Unrecognized message type.")
        );
    }

    public abstract ContractTransaction newPutTransaction(Key client);

}
