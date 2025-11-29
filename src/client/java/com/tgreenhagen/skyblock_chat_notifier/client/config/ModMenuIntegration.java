package com.tgreenhagen.skyblock_chat_notifier.client.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (Screen parent) -> ModConfigScreenFactory.create(
                parent,
                ConfigManager.get(),
                ConfigManager::save
        );
    }
}
