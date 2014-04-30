package org.reefy.core.contract;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class AcceptContractMessage implements ContractMessage {
    public static final String TYPE = "ACCEPT";

    public static AcceptContractMessage get() {
        return new AcceptContractMessage();
    }

    private AcceptContractMessage() {}

    @Override
    public String getType() {
        return TYPE;
    }
}
