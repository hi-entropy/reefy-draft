package org.reefy.core.contract.free;

import org.reefy.core.Key;
import org.reefy.core.Value;
import org.reefy.core.contract.ContractClient;
import org.reefy.core.contract.transaction.ContractTransaction;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class FreeContractClient implements ContractClient {

    @Override
    public ContractTransaction put(Key server, Key key, Value value) {
        return new SucceedContractTransaction(server);
    }
}
