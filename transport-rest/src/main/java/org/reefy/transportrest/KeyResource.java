package org.reefy.transportrest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

import org.reefy.transportrest.api.AppServerHandler;
import org.reefy.transportrest.api.Key;
import org.reefy.transportrest.api.RawKey;
import org.reefy.transportrest.api.RawValue;
import org.reefy.transportrest.api.Value;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
    @PUT
    public Response handleFooPut(final @PathParam("key") String keyHex, final @FormParam("value") String valueString) throws InterruptedException, ExecutionException, TimeoutException, IOException {
        final Key key;
        try {
            key = RawKey.from(parseHexBinary(keyHex));
        } catch (Exception e) {
            throw new WebApplicationException(
                    e,
                    Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build()
            );
        }
        final Value value = new RawValue(valueString.getBytes());

        // TODO: make this async
        final AppServerHandler.PutResponse<RestContact> putResponse =
                appServerHandler.put(key, value).get(10000, TimeUnit.MILLISECONDS);

        if (putResponse.succeeded()) {
            // TODO: use a real URI
            return Response.created(URI.create("fakeuri")).build();
        }

        if (putResponse.redirected() != null) {
            final RestContact contact = putResponse.redirected();

            // TODO: this is an ugly way to do a redirect
            final String request = "put/" + keyHex;
            // TODO: not encoding this url opens us up to a URL-splitting vulnerability. also elsewhere?
            // TODO: don't throw an IOException, we can probably actually guarantee that there will be no JSON error
            return Response.status(Response.Status.MOVED_PERMANENTLY)
                    .header("Location", contact.toUrl(request).toString())
                    .entity(mapper.writeValueAsString(contact))
                    .build();
        }

        if (putResponse.failed() != null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(putResponse.failed().getMessage())
                    .build();
        }

        throw new AssertionError("unreachable case");

    }

    // TODO: this should not return Object
    // TODO: this should not throw all these exceptions
    @GET
    public Response handleFooGet(final @PathParam ("key") String keyHex) throws InterruptedException, ExecutionException, TimeoutException, IOException {

        final Key key;
        try {
            key = RawKey.from(parseHexBinary(keyHex));
        } catch (Exception e) {
            throw new WebApplicationException(
                    e,
                    Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build()
            );
        }

        // TODO: make this async

        final AppServerHandler.GetResponse<RestContact> getResponse =
                appServerHandler.get(key).get(10000, TimeUnit.MILLISECONDS);


        if (getResponse.succeeded() != null) {
            return Response.ok(printHexBinary(getResponse.succeeded().getBytes())).build();
        }

        if (getResponse.redirected() != null) {
            final RestContact contact = getResponse.redirected();

            // TODO: this is an ugly way to do a redirect
            final String request = "get/" + keyHex;
            // TODO: not encoding this url opens us up to a URL-splitting vulnerability. also elsewhere?
            // TODO: don't throw an IOException, we can probably actually guarantee that there will be no JSON error
            return Response.status(Response.Status.MOVED_PERMANENTLY)
                    .header("Location", contact.toUrl(request).toString())
                    .entity(mapper.writeValueAsString(contact))
                    .build();
        }

        if (getResponse.failed() != null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(getResponse.failed().getMessage())
                    .build();
        }

        throw new AssertionError("unreachable case");
    }
}