package org.reefy.transportrest.api;

import org.joda.time.Duration;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public interface AppClient {
    void get(Key key, GetCallback callback);

    public interface GetCallback {
        public abstract void succeed(Value value);

        public abstract void fail(Throwable e);
    }

    void put(Key key, Value value, Duration duration, PutCallback callback);

    public interface PutCallback {
        public abstract void succeed();

        public abstract void fail(Throwable e);
    }
}
