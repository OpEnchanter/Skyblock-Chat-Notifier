package com.tgreenhagen.skyblock_chat_notifier.client;

import com.tgreenhagen.skyblock_chat_notifier.client.config.ConfigManager;
import com.tgreenhagen.skyblock_chat_notifier.client.config.ModConfig;
import com.tgreenhagen.skyblock_chat_notifier.client.event.ChatListener;
import net.fabricmc.api.ClientModInitializer;

import java.util.ArrayList;

public class Skyblock_chat_notifierClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Load config
        ConfigManager.load();
        System.out.println("[Skyblock Chat Notifier] Config loaded!");

        ModConfig config = ConfigManager.get();
        if (config.triggers == null || config.triggers.isEmpty()) {
            System.out.println("[Skyblock Chat Notifier] No triggers found! Creating triggers list!");
            config.triggers = new ArrayList<>();
        }

        // Register listeners
        ChatListener.register();
        System.out.println("[Skyblock Chat Notifier] Listeners registered!");
    }
}
