package com.tgreenhagen.skyblock_chat_notifier.client.config.pattern;

public class TriggerPattern {
    public String trigger;
    public String message;
    public boolean enabled;

    public TriggerPattern(String trigger, String message, boolean enabled) {
        this.trigger = trigger;
        this.message = message;
        this.enabled = enabled;
    }
}
