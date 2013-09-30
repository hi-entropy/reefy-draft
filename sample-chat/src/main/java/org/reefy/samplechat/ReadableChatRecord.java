package org.reefy.samplechat;

import org.joda.time.DateTime;

import java.util.List;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public interface ReadableChatRecord {

    public List<ChatItem> getChatItems();

    public DateTime getNewestDateTime();

    List<ChatItem> getChatItemsAfter(DateTime newestDateTime);
}
