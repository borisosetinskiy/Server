package com.ob.server.handlers;

import com.ob.server.AttributeKeys;
import com.ob.server.ServerLogger;
import com.ob.server.handlers.websocket.WebSocketUtil;
import com.ob.server.session.RequestSession;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.ReferenceCountUtil;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class ProcessHandler extends MessageToMessageDecoder<Object> {
    private final AtomicLong totalRequest = new AtomicLong();

    private AtomicLong time = new AtomicLong(0);
    @Override
    protected void decode(ChannelHandlerContext ctx
            , Object message
            , List<Object> list) throws Exception {
        if (message != null) {
            if (ServerLogger.loggerMessage.isDebugEnabled()) {
                if(time.get() == 0){
                    time.addAndGet(System.currentTimeMillis());
                }
                ServerLogger.loggerMessage.debug("Channel {}, requests {}, ip {}, time {} sec"
                        , ctx.channel().id().asShortText()
                        , totalRequest.incrementAndGet()
                        , ctx.channel().remoteAddress()
                , (System.currentTimeMillis() - time.get())/1000);
            }
            io.netty.util.Attribute<RequestSession> attribute
                    = ctx.channel()
                    .attr(AttributeKeys.REQUEST_SESSION_ATTR_KEY);
            if (attribute == null) {
                throw new Exception("No session");
            }
            RequestSession requestSession = attribute.get();
            if (requestSession == null) {
                throw new Exception("No session");
            }
            requestSession.onRead(ctx, message);
        }
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        io.netty.util.Attribute<RequestSession> attribute = ctx.channel().attr(AttributeKeys.REQUEST_SESSION_ATTR_KEY);
        if (attribute != null) {
            RequestSession requestSession = attribute.get();
            if (requestSession != null) {
                try {
                    requestSession.onClose();
                } catch (Exception e) {
                }
            }
        }
    }
}
