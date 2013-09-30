package org.reefy.samplechat;

import org.joda.time.DateTime;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class ChatItem {
    private final DateTime dateTime;
    private final String message;

    public ChatItem(DateTime dateTime, String message) {
        this.dateTime = dateTime;
        this.message = message;
    }

    @Override
    public String toString() {
        return dateTime.toString() + ": " + message;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public String getMessage() {
        return message;
    }
}