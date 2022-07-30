package com.hawolt.proxy;

import com.hawolt.http.IRequestModifier;
import com.hawolt.http.Method;
import com.hawolt.logger.Logger;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.io.IOException;
import java.util.*;

/**
 * Created: 30/07/2022 15:48
 * Author: Twitter @hawolt
 **/

public class BasicProxyServer {
    private final Map<Method, List<IRequestModifier>> map = new HashMap<>();

    private final String target;

    public BasicProxyServer(int port, String target) {
        this.target = target;
        Javalin javalin = Javalin.create().start("127.0.0.1", port);
        javalin.get("*", this::forward);
        javalin.head("*", this::forward);
        javalin.post("*", this::forward);
        javalin.put("*", this::forward);
        javalin.delete("*", this::forward);
        javalin.options("*", this::forward);
        javalin.patch("*", this::forward);
    }

    private void forward(Context context) {
        Method method = Method.valueOf(context.method().toUpperCase());
        String query = context.queryString();
        String destination = String.format("%s%s%s", target, context.path(), query != null ? "?" + query : "");
        String body = context.body();
        boolean output = body.length() > 0;
        ProxyRequest initial = new ProxyRequest(destination, method, output);
        for (String header : context.headerMap().keySet()) {
            if (header.equalsIgnoreCase("Host") || header.equalsIgnoreCase("Accept-Encoding")) continue;
            initial.addHeader(header, context.headerMap().get(header));
        }
        initial.setOutput(output);
        initial.setBody(body);
        ProxyResponse response = null;
        if (map.containsKey(method)) {
            List<IRequestModifier> list = map.get(method);
            for (IRequestModifier modifier : list) {
                try {
                    initial = modifier.onBeforeRequest(initial);
                    response = modifier.onResponse(new ProxyResponse(initial.execute()));
                } catch (Exception e) {
                    modifier.onException(e);
                    Logger.error(e);
                }
            }
        }
        if (response == null) {
            try {
                response = new ProxyResponse(initial.execute());
            } catch (IOException e) {
                Logger.error(e);
            }
        }
        ProxyResponse complete = Objects.requireNonNull(response);
        context.status(complete.getCode());
        String content = new String(complete.getBody());
        context.header("Content-Length", String.valueOf(content.length()));
        String type = context.header("Content-Type");
        if (type != null) context.header("Content-Type", type);
        context.result(content);
    }

    public void register(IRequestModifier modifier, Method... methods) {
        for (Method method : methods) {
            if (method == Method.TRACE || method == Method.CONNECT) {
                throw new UnsupportedRequestMethodException(method);
            } else {
                if (!map.containsKey(method)) map.put(method, new LinkedList<>());
                map.get(method).add(modifier);
            }
        }
    }
}
