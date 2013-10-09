package org.reefy.transportrest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
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

    public RestContact(
            @JsonProperty("key") Key key,
            @JsonProperty("ipAddress") String ipAddress,
            @JsonProperty("port") int port
    ) {
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

    @Override
    public int hashCode() {
        // TODO: make this
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) { return false; }

        final RestContact restContact = (RestContact) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(ipAddress, restContact.ipAddress)
                .append(port, restContact.port)
                .isEquals();
    }
}
