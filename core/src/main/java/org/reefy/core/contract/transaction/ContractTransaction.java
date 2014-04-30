package org.reefy.core.contract.transaction;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;

import org.reefy.core.Key;
import org.reefy.core.contract.ContractMessage;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public interface ContractTransaction {
    public Key getCounterparty();

    //@Nullable
    public ListenableFuture<ContractMessage> nextMessage();
}
