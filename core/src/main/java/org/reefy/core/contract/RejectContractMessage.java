package org.reefy.core.contract;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class RejectContractMessage implements ContractMessage {
    public static final int code = -1;

    private final String message;

    public RejectContractMessage(String message) {

        this.message = message;
    }
}
