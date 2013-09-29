package org.reefy.transportrest;

import org.reefy.transportrest.api.Key;
import org.reefy.transportrest.api.Value;
import org.reefy.transportrest.api.transport.TransportClient;

 /**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class RestTransportClient implements TransportClient<RestContact> {
     @Override
     public void get(RestContact contact, Key key, GetCallback callback) {
         //To change body of implemented methods use File | Settings | File Templates.
     }

     @Override
     public void put(RestContact contact, Key key, Value value, PutCallback callback) {
         //To change body of implemented methods use File | Settings | File Templates.
     }
 }
