package org.reefy.core.store;

/**
 * Created with IntelliJ IDEA.
 * User: kernylicious
 * Date: 9/22/13
 * Time: 9:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class StoreException extends Exception {
    public StoreException(String message) {
        super(message);
    }

    public StoreException(Throwable e) {
        super(e);
    }
}
