package com.wang125510.cleaner.config;

import java.util.ArrayList;
import java.util.List;

public class CleanerConfig {
    private boolean worldFileEnabled = false;
    private List<String> worldFileDeletePatterns = new ArrayList<>();
    private boolean serverFileEnabled = false;
    private List<String> serverFileDeletePatterns = new ArrayList<>();
    private List<String> whiteList = new ArrayList<>();

    public boolean isWorldFileEnabled() {
        return worldFileEnabled;
    }

    public void setWorldFileEnabled(boolean enabled) {
        this.worldFileEnabled = enabled;
    }

    public List<String> getWorldFileDeletePatterns() {
        return worldFileDeletePatterns;
    }

    public boolean isServerFileEnabled() {
        return serverFileEnabled;
    }

    public void setServerFileEnabled(boolean serverFileEnabled) {
        this.serverFileEnabled = serverFileEnabled;
    }

    public List<String> getServerFileDeletePatterns() {
        return serverFileDeletePatterns;
    }

    public List<String> getWhiteList() {
        return whiteList;
    }
}