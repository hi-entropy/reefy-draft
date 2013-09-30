package org.reefy.samplechat;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class ChatRecord implements ReadableChatRecord, Serializable {

    private final int maxSize;

    // Yes I know this is not as efficient as a circular buffer, deal with it.
    private final List<ChatItem> chatItems = Lists.newArrayList();
    private int size = 0;

    public ChatRecord(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public List<ChatItem> getChatItems() {
        return Collections.unmodifiableList(chatItems);
    }

    @Override
    public DateTime getNewestDateTime() {
        if (chatItems.isEmpty()) {
            return new DateTime(0);
        }

        return chatItems.get(chatItems.size() - 1).getDateTime();
    }

    @Override
    public List<ChatItem> getChatItemsAfter(DateTime dateTime) {
        final List<ChatItem> chatItemsAfter = Lists.newArrayList();
        for (ChatItem chatItem : chatItems) {
            if (chatItem.getDateTime().isAfter(dateTime)) {
                chatItemsAfter.add(chatItem);
            }
        }
        return chatItemsAfter;
    }

    public void add(ChatItem chatItem) {
        chatItems.add(chatItem);
        size += chatItem.getMessage().length();

        prune();
    }

    private void prune() {
        while (size > maxSize) {
            size -= chatItems.get(0).getMessage().length();
            chatItems.remove(0);
        }
    }
}
