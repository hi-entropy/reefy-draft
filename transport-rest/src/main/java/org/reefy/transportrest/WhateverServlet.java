package org.reefy.transportrest;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
import org.reefy.transportrest.api.AppServerHandler;
import org.reefy.transportrest.api.Key;
import org.reefy.transportrest.api.RawKey;
import org.reefy.transportrest.api.Value;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.xml.bind.DatatypeConverter.parseHexBinary;
import static javax.xml.bind.DatatypeConverter.printHexBinary;

public class WhateverServlet extends HttpServlet {

    private final AppServerHandler appServerHandler;

    public WhateverServlet(AppServerHandler appServerHandler) {
        this.appServerHandler = appServerHandler;
    }

    @Override
    protected void service(HttpServletRequest req, final HttpServletResponse resp)
        throws ServletException, IOException {

        resp.setContentType("text/plain");

        // TODO: *vomits a little*
        final int length = req.getRequestURI().length();
        final String keyHex = req.getRequestURI().substring(length - 40, length);
        final Key key = new RawKey(parseHexBinary(keyHex));
        appServerHandler.get(key, new AppServerHandler.GetCallback() {
            @Override
            public void present(Value value) {
                resp.setStatus(HttpServletResponse.SC_OK);
                try {
                    resp.getWriter().append(printHexBinary(value.getBytes().array()));
                } catch (IOException e) {
                    // TODO: log this I guess?
                    e.printStackTrace();
                }
            }

            @Override
            public void notFound() {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }

            @Override
            public void fail(Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                try {
                    resp.getWriter().append(e.getMessage());
                } catch (IOException e2) {
                    // TODO: log this I guess?
                    e.printStackTrace();
                }
            }
        });
    }

}
