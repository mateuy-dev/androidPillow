package com.mateuyabar.android.pillow.view;

import android.content.Context;

import com.android.volley.NoConnectionError;
import com.mateuyabar.android.pillow.Listeners;
import com.mateuyabar.android.pillow.PillowError;
import com.mateuyabar.android.pillow.data.sync.CommonListeners;
import com.mateuyabar.android.pillow.view.message.DisplayMessages;
import com.mateuyabar.android.pillow.views.R;
import com.mateuyabar.util.exceptions.BreakFastException;

/**
 * Created by mateuyabar on 16/11/15.
 */
public class CommonViewListeners extends CommonListeners {
    public static class ErrorListenerWithNoConnectionToast  implements Listeners.ViewErrorListener {
        Context context;

        public ErrorListenerWithNoConnectionToast(Context context) {
            this.context = context;
        }

        @Override
        public void onErrorResponse(PillowError error) {
            if(error.getCause() instanceof NoConnectionError){
                DisplayMessages.error(context, R.string.no_connection_error);
            } else {
                throw new BreakFastException(error.getCause());
            }
        }
    }


    public static class DummyToastListener<T> implements Listeners.ViewListener<T> {
        String text;
        Context context;
        public DummyToastListener(Context context, String text) {
            this.context = context;
            this.text= text;
        }
        @Override
        public void onResponse(T response) {
            DisplayMessages.info(context, text);
        }
    }

    public static class DisplayMessageListener<T> implements Listeners.ViewListener<T> {
        int messageId;
        Context context;

        public DisplayMessageListener(Context context, int messageId) {
            this.context = context;
            this.messageId = messageId;
        }

        @Override
        public void onResponse(T response) {
            DisplayMessages.info(context, messageId);
        }
    }
}
