package org.reefy.transportrest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.google.inject.Inject;
import org.reefy.transportrest.api.AppServerHandler;
import org.reefy.transportrest.api.Key;
import org.reefy.transportrest.api.RawKey;
import org.reefy.transportrest.api.Value;
import org.reefy.transportrest.api.transport.Contact;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static javax.xml.bind.DatatypeConverter.parseHexBinary;
import static javax.xml.bind.DatatypeConverter.printHexBinary;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
// TODO: figure out how to have sub-resources so we don't have to prepend this with 0/
@Path("/0/key/{key}")
@Produces(MediaType.TEXT_PLAIN)
public class KeyResource {

    private static final ObjectMapper mapper = new ObjectMapper().enableDefaultTyping();

    private final AppServerHandler<RestContact> appServerHandler;

    @Inject
    public KeyResource(AppServerHandler<RestContact> appServerHandler) {
        this.appServerHandler = appServerHandler;
    }

    // TODO: this should not return Object
    @GET
    public Response handleFooGet(final @PathParam ("key") String keyHex) {

        final Key key;
        try {
            key = new RawKey(parseHexBinary(keyHex));
        } catch (Exception e) {
            throw new WebApplicationException(
                    e,
                    Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build()
            );
        }

        // TODO: wrap value/contact in some AppServerHandlerResponse POJO
        final SettableFuture<Response> serverHandlerFuture = SettableFuture.create();

        appServerHandler.get(key, new AppServerHandler.GetCallback<RestContact>() {
            @Override
            public void present(Value value) {
                serverHandlerFuture.set(Response.ok(printHexBinary(value.getBytes())).build());
            }

            @Override
            public void redirect(RestContact contact) {
                // TODO: this is an ugly way to do a redirect
                final String request = "get/" + keyHex;
                // TODO: not encoding this url opens us up to a URL-splitting vulnerability
                try {
                    serverHandlerFuture.set(
                            Response.status(Response.Status.MOVED_PERMANENTLY)
                                    .header("Location", contact.toUrl(request).toString())
                                    .entity(mapper.writeValueAsString(contact))
                                    .build()
                    );
                } catch (IOException e) {
                    serverHandlerFuture.setException(e);
                }
            }

            @Override
            public void notFound() {
                serverHandlerFuture.set(
                        Response.status(Response.Status.NOT_FOUND)
                                .entity("key not found")
                                .build()
                );
            }

            @Override
            public void fail(Exception e) {
                serverHandlerFuture.set(
                        Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                .entity(e.getMessage())
                                .build()
                );
            }
        });

        try {
            return serverHandlerFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new AssertionError(e);
        }
    }
}