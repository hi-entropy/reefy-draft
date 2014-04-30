package org.reefy.core.contract.transaction;

import org.reefy.core.Key;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public abstract class AbstractContractTransaction implements ContractTransaction {

    private final Key counterparty;

    public AbstractContractTransaction(Key counterparty) {
        this.counterparty = counterparty;
    }

    @Override
    public Key getCounterparty() {
        return counterparty;
    }
}
