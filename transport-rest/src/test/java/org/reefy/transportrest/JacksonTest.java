package org.reefy.transportrest;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.reefy.core.transport.Contact;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class JacksonTest {
    @Test
    public void testStuff() throws IOException {
        final ObjectMapper mapper = new ObjectMapper().enableDefaultTyping();

        final RestTransportFactory transportFactory = new RestTransportFactory();

        final RestContact contact = transportFactory.buildMockContact();

        final String serialized = mapper.writeValueAsString(contact);

        System.out.println(serialized);

        final RestContact deserialized = mapper.readValue(serialized, RestContact.class);

        assertThat(deserialized, is(contact));
    }

}
