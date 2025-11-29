package com.tgreenhagen.skyblock_chat_notifier.client.event;

import com.tgreenhagen.skyblock_chat_notifier.client.config.ConfigManager;
import com.tgreenhagen.skyblock_chat_notifier.client.config.ModConfig;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ChatListener {
    public static void register() {
        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            onChatMessage(message.getString());
        });
    }

    private static void onChatMessage(String message) {
        ModConfig config = ConfigManager.get();
        if (config.enabled) {
            config.triggers.forEach(trigger -> {
                if (trigger.enabled) {
                    if (message.contains(trigger.trigger)) {
                        MinecraftClient.getInstance().inGameHud.setTitle(Text.of(trigger.message));
                    }
                }
            });

        }
    }
}
