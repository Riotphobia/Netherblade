package com.hawolt.mitm.impl;

import com.hawolt.http.proxy.ProxyRequest;
import com.hawolt.http.proxy.ProxyResponse;
import com.hawolt.mitm.CommunicationType;
import com.hawolt.mitm.RewriteModule;
import com.hawolt.mitm.rule.RewriteRule;

import java.util.List;

/**
 * Created: 22/11/2022 04:01
 * Author: Twitter @hawolt
 **/

public class RequestModule extends RewriteModule<ProxyRequest> {


    @Override
    protected ProxyRequest rewriteHeaders(ProxyRequest communication, RewriteRule rule) {
        return null;
    }

    @Override
    protected ProxyRequest rewriteQuery(ProxyRequest communication, RewriteRule rule) {
        return null;
    }

    @Override
    protected ProxyRequest rewriteURL(ProxyRequest communication, RewriteRule rule) {
        return null;
    }
}
