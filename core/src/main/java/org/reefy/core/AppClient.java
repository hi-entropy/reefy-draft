package org.reefy.core;

import org.joda.time.Duration;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public interface AppClient {
    void get(Key key, GetCallback callback);

    public interface GetCallback {
        public void succeed(Value value);

        public void notFound();

        public void fail(Throwable e);
    }

    void put(Key key, Value value, Duration duration, PutCallback callback);

    public interface PutCallback {
        public void succeed();

        public void fail(Throwable e);
    }
}
