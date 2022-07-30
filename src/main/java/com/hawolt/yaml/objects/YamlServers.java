package com.hawolt.yaml.objects;

import java.util.Map;

/**
 * Created: 30/07/2022 18:12
 * Author: Twitter @hawolt
 **/

public class YamlServers {
    private final String config, email, entitlement, queue, ledge, platform;

    @SuppressWarnings("all")
    public YamlServers(Object o) {
        Map<String, Map<String, Object>> map = (Map<String, Map<String, Object>>) o;
        this.config = map.get("client_config").get("client_config_url").toString();
        this.email = map.get("email_verification").get("external_url").toString();
        this.entitlement = map.get("entitlements").get("entitlements_url").toString();
        this.queue = map.get("lcds").get("login_queue_url").toString();
        this.ledge = map.get("league_edge").get("league_edge_url").toString();
        this.platform = map.get("player_platform_edge").get("player_platform_edge_url").toString();
    }

    public String getConfig() {
        return config;
    }

    public String getEmail() {
        return email;
    }

    public String getEntitlement() {
        return entitlement;
    }

    public String getQueue() {
        return queue;
    }

    public String getLedge() {
        return ledge;
    }

    public String getPlatform() {
        return platform;
    }
}
