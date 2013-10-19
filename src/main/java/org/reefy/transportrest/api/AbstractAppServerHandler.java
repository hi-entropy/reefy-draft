package org.reefy.transportrest.api;

import com.google.common.base.Preconditions;
import com.sun.istack.internal.Nullable;
import org.reefy.transportrest.api.transport.Contact;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class AbstractAppServerHandler {
    public static <C extends Contact> AppServerHandler.PutResponse<C> succeedPutResponse() {
        return new PutResponse<C>(true, null, null);
    }

    public static <C extends Contact> AppServerHandler.PutResponse<C> redirectPutResponse(C redirect) {
        return new PutResponse<C>(false, redirect, null);
    }

    public static <C extends Contact> AppServerHandler.PutResponse<C> failPutResponse(Exception exception) {
        return new PutResponse<C>(false, null, exception);
    }

    public static class PutResponse<C extends Contact> implements AppServerHandler.PutResponse<C> {

        private final boolean succeeded;

        @Nullable
        private final C redirected;

        @Nullable
        private final Exception failed;

        private PutResponse(boolean succeeded, @Nullable C redirected, @Nullable Exception failed) {
            int total = 0;
            total += (succeeded ? 1 : 0);
            total += (redirected != null ? 1 : 0);
            total += (failed != null ? 1 : 0);
            Preconditions.checkArgument(total == 1, "Exactly one input must be ");

            this.succeeded = succeeded;
            this.redirected = redirected;
            this.failed = failed;
        }

        @Override
        public boolean succeeded() {
            return succeeded;
        }

        @Nullable
        @Override
        public C redirected() {
            return redirected;
        }

        @Nullable
        @Override
        public Exception failed() {
            return failed;
        }
    }


    public static <C extends Contact> AppServerHandler.GetResponse<C> succeedGetResponse(Value value) {
        return new GetResponse<C>(value, null, null);
    }

    public static <C extends Contact> AppServerHandler.GetResponse<C> redirectGetResponse(C redirect) {
        return new GetResponse<C>(null, redirect, null);
    }

    public static <C extends Contact> AppServerHandler.GetResponse<C> failGetResponse(Exception exception) {
        return new GetResponse<C>(null, null, exception);
    }

    public static class GetResponse<C extends Contact> implements AppServerHandler.GetResponse<C> {

        private final Value succeeded;

        @Nullable
        private final C redirected;

        @Nullable
        private final Exception failed;

        private GetResponse(Value succeeded, @Nullable C redirected, @Nullable Exception failed) {
            int total = 0;
            total += succeeded != null ? 1 : 0;
            total += redirected != null ? 1 : 0;
            total += failed != null ? 1 : 0;
            Preconditions.checkArgument(total == 1, "Exactly one input must be ");

            this.succeeded = succeeded;
            this.redirected = redirected;
            this.failed = failed;
        }

        @Override
        public Value succeeded() {
            return succeeded;
        }

        @Nullable
        @Override
        public C redirected() {
            return redirected;
        }

        @Nullable
        @Override
        public Exception failed() {
            return failed;
        }
    }

}
