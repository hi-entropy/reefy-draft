package org.reefy.transportrest;

import org.reefy.transportrest.api.AbstractContact;
import org.reefy.transportrest.api.Key;
import org.reefy.transportrest.api.RawKey;
import org.reefy.transportrest.api.transport.Contact;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class RestContact extends AbstractContact {

    private final String ipAddress;
    private final int port;

    public RestContact(Key key, String ipAddress, int port) {
        super(key);
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public static RestContact fromString(String string) {
        // TODO(PK): Implement this method
        return new RestContact(RawKey.pseudorandom(), "", -1);
    }
}
