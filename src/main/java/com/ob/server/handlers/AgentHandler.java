
package com.ob.server.handlers;

import com.ob.server.PrintUtil;
import com.ob.server.ServerLogger;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.ReferenceCountUtil;

import java.net.InetSocketAddress;
import java.util.List;

@ChannelHandler.Sharable
public class AgentHandler
extends MessageToMessageDecoder<Object> {

    protected void decode(ChannelHandlerContext channelHandlerContext, Object o, List<Object> list) throws Exception {
        if (o instanceof HttpRequest) {
            HttpRequest request = (HttpRequest)o;
            StringBuilder stringBuilder = new StringBuilder();
            if(ServerLogger.agentLogger.isDebugEnabled()){
                ServerLogger.agentLogger.debug(
                        "Channel {}, Address {}, Request {}"
                        , channelHandlerContext.channel().id().asShortText()
                        , ((InetSocketAddress)channelHandlerContext.channel().remoteAddress())
                                .getAddress()
                        , PrintUtil.appendRequest(stringBuilder, request));
            }
        }
        list.add(ReferenceCountUtil.retain((Object)o));
    }
}

