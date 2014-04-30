package org.reefy.core.contract.transaction;

import com.google.common.collect.Maps;

import org.apache.commons.lang3.tuple.Pair;
import org.reefy.core.Key;

import java.util.concurrent.ConcurrentMap;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
//@ThreadSafe
public abstract class TransactionMap<T extends ContractTransaction> {
    private final ConcurrentMap<Pair<Key, Key>, T> transactions = Maps.newConcurrentMap();

    public T get(Key counterparty, Key key) {
        final T newTransaction = newTransaction();
        final T oldTransaction =
            transactions.putIfAbsent(Pair.of(counterparty, key), newTransaction);
        if (oldTransaction != null) {
            return oldTransaction;
        }
        return newTransaction;
    }

    public abstract T newTransaction();
}
