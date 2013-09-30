package org.reefy.samplechat;

import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.sun.istack.internal.Nullable;

import org.reefy.transportrest.api.Key;

import java.util.List;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class Buddy {
    private final Key<ReadableChatRecord> key;
    private final String name;

    @Nullable
    private ReadableChatRecord chatRecord;

    public Buddy(Key key, String name) {
        this.key = key;
        this.name = name;
    }

    public Key getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public List<ChatItem> updateChatRecord(ReadableChatRecord chatRecord) {


        final List<ChatItem> newChatItems =
            this.chatRecord.getChatItemsAfter(chatRecord.getNewestDateTime());

        this.chatRecord = chatRecord;

        return newChatItems;
    }
}
