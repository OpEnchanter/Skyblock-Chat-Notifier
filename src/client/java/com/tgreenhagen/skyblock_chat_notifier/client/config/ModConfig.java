package com.tgreenhagen.skyblock_chat_notifier.client.config;

import com.tgreenhagen.skyblock_chat_notifier.client.config.pattern.TriggerPattern;

import java.util.ArrayList;
import java.util.List;

public class ModConfig {
    public boolean enabled = true;
    public boolean hypixelOnly = true;
    public List<TriggerPattern> triggers;
}
