package com.geostar.geostack.git_branch_manager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
public class MyConfig {

    @Value("${server.port:8080}")
    private int port;

    @Value("${git-save-location:/git-branch-manager/src/main/resources/application-git.yml}")
    private String gitSaveLocation;

    public int getPort() {
        return port;
    }

    public String getGitSaveLocation() {
        return gitSaveLocation;
    }
}
