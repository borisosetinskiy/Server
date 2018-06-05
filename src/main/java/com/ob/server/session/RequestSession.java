package com.ob.server.session;


import com.ob.server.resolvers.ChannelRequest;


/**
 * Created by boris on 11.04.2016.
 */
public interface RequestSession extends Session{
    ChannelRequest getChannelRequest();
    void onRead(final Object message);
    void onWrite(final Object message);
    RequestSession EMPTY = new RequestSession(){

        @Override
        public void onWrite(Object message) {

        }
        @Override
        public String getSessionId() {
            return null;
        }

        @Override
        public void onOpen() {

        }

        @Override
        public void onClose() {

        }

        @Override
        public void onRead(Object message) {

        }

        @Override
        public ChannelRequest getChannelRequest() {
            return null;
        }

    };
}
