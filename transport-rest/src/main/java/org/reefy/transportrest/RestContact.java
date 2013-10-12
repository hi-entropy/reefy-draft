package org.reefy.transportrest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.reefy.transportrest.api.AbstractContact;
import org.reefy.transportrest.api.Key;
import org.reefy.transportrest.api.RawKey;
import org.reefy.transportrest.api.transport.Contact;

import javax.ws.rs.core.UriBuilder;
import java.io.Serializable;
import java.net.URI;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class RestContact extends AbstractContact {

    private static final String REST_API_VERSION = "0";

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

    public URI toUrl(String request) {
        return UriBuilder.fromPath("http://{ip}:{port}/{version}/{request}")
                .buildFromEncodedMap(ImmutableMap.of(
                        "ip", ipAddress,
                        "port", port,
                        "version", REST_API_VERSION,
                        "request", request
                ));
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
