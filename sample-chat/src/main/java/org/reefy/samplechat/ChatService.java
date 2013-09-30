package org.reefy.samplechat;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.common.util.concurrent.AbstractScheduledService;

import org.reefy.transportrest.api.Value;
import org.reefy.transportrest.api.store.Store;
import org.reefy.transportrest.api.store.StoreException;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class ChatService extends AbstractIdleService {

    private final List<Buddy> buddies = Lists.newArrayList();
    private final Store store;

    @Override
    protected void startUp() throws Exception {
        final AbstractScheduledService chatChecker = new AbstractScheduledService() {
            @Override
            protected void runOneIteration() throws Exception {
                for (final Buddy buddy : buddies) {
                    store.get(buddy.getKey(), new Store.GetCallback<Object>() {
                        @Override
                        public void succeed(Value<Object> value) {
                            try {
                                final ChatRecord chatRecord = (ChatRecord) (
                                    new ObjectInputStream(
                                        new ByteArrayInputStream(value.getBytes().array())
                                    ).readObject()
                                );
                                for (ChatItem newChatItem : buddy.updateChatRecord(chatRecord)) {
                                    System.out.println(
                                        buddy.getName() + " " + newChatItem.toString()
                                    );
                                }
                            }

                            // TODO: redo as java 7 multiple exception catching
                            catch (IOException e) {
                                System.out.println(e);
                            } catch (ClassNotFoundException e) {
                                System.out.println(e);
                            }
                        }


                        @Override
                        public void fail(StoreException e) {
                            System.out.println(e);
                        }
                    });
                }
            }

            @Override
            protected Scheduler scheduler() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        }
    }

    @Override
    protected void shutDown() throws Exception {
        store.close();
    }
}
