package com.wang125510.cleaner.config;

import java.util.ArrayList;
import java.util.List;

public class CleanerConfig {
    private boolean enabled = false;
    private List<String> deletePatterns = new ArrayList<>();
    private List<String> whiteList = new ArrayList<>();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getDeletePatterns() {
        return deletePatterns;
    }

    public List<String> getWhiteList() {
        return whiteList;
    }
}