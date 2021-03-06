package org.reefy.transportrest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.AbstractIdleService;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.reefy.core.Key;
import org.reefy.core.RawValue;
import org.reefy.core.Value;
import org.reefy.core.transport.TransportClient;
import org.reefy.core.transport.TransportException;
import org.reefy.core.transport.ValueNotFoundException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import static javax.xml.bind.DatatypeConverter.parseHexBinary;
import static javax.xml.bind.DatatypeConverter.printHexBinary;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class RestTransportClient
        extends AbstractIdleService implements TransportClient<RestContact> {

    private static final ObjectMapper mapper = new ObjectMapper().enableDefaultTyping();

    @Override
    protected void startUp() throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void shutDown() throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void find(RestContact contact, Key key, FindCallback callback) {
        //To change body of implemented methods use File | Settings | File Templates.
    }



    @Override
    public void put(RestContact contact, Key key, Value value, PutCallback callback) {
        Preconditions.checkState(isRunning());

        // TODO: Make sure this doesn't have security holes
        // TODO: This is probably not the best way to compose this URL
        final String request = "key/" + printHexBinary(key.getBytes());
        final HttpPut httpPut = new HttpPut(contact.toUrl(request));
        final List<NameValuePair> formParameters = ImmutableList.<NameValuePair>of(
                new BasicNameValuePair("value", new String(value.getBytes()))
        );
        try {
            httpPut.setEntity(new UrlEncodedFormEntity(formParameters));
        } catch (UnsupportedEncodingException e) {
            // TODO: can this actually be thrown?
            throw new AssertionError(e);
        }

        // Don't follow redirects automatically, we need to do it manually.
        final CloseableHttpClient httpClient = HttpClients.custom().disableRedirectHandling().build();
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPut);
        } catch (IOException e) {
            callback.fail(new TransportException(e));
            return;
        }

        // TODO: rewrite with Java 7
        try {
            final int statusCode = response.getStatusLine().getStatusCode();
            final HttpEntity entity = response.getEntity();

            // Success getting the value
            if (statusCode == HttpServletResponse.SC_CREATED) {
                callback.succeed();
                return;
            }

            // Redirect
            if (statusCode == HttpServletResponse.SC_MOVED_PERMANENTLY) {
                callback.redirect(mapper.readValue(EntityUtils.toString(entity), RestContact.class));
                return;
            }

            callback.fail(new TransportException(
                    "Unexpected status code: " + statusCode + "\n" + EntityUtils.toString(entity)
            ));

            // always do something useful with the response body
            // and ensure it is fully consumed
            // EntityUtils.consume(entity2);
        } catch (IOException e) {
            callback.fail(new TransportException(e));
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }

    @Override
    public void get(RestContact contact, Key key, GetCallback<RestContact> callback) {
        Preconditions.checkState(isRunning());

        // TODO: Make sure this doesn't have security holes
        // TODO: This is probably not the best way to compose this URL
        final String request = "key/" + printHexBinary(key.getBytes());
        final HttpGet httpGet = new HttpGet(contact.toUrl(request));

        // Don't follow redirects automatically, we need to do it manually.
        final CloseableHttpClient httpClient = HttpClients.custom().disableRedirectHandling().build();
        CloseableHttpResponse response2 = null;
        try {
            response2 = httpClient.execute(httpGet);
        } catch (IOException e) {
            callback.fail(new TransportException(e));
            return;
        }

        // TODO: rewrite with Java 7
        try {
            final int statusCode = response2.getStatusLine().getStatusCode();
            HttpEntity entity2 = response2.getEntity();

            // Success getting the value
            if (statusCode == HttpServletResponse.SC_OK) {
                callback.present(new RawValue(parseHexBinary(EntityUtils.toString(entity2))));
                return;
            }

            // Redirect
            if (statusCode == HttpServletResponse.SC_MOVED_PERMANENTLY) {
                callback.redirect(mapper.readValue(EntityUtils.toString(entity2), RestContact.class));
                return;
            }

            // Not found
            if (statusCode == HttpServletResponse.SC_NOT_FOUND) {
                callback.fail(new TransportException(new ValueNotFoundException()));
                return;
            }

            callback.fail(new TransportException(
                    "Unexpected status code: " + statusCode + "\n" + EntityUtils.toString(entity2)
            ));

            // always do something useful with the response body
            // and ensure it is fully consumed
            // EntityUtils.consume(entity2);
        } catch (IOException e) {
            callback.fail(new TransportException(e));
        } finally {
            try {
                response2.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }
}
