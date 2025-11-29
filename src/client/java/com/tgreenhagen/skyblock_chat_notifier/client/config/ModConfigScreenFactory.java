package com.tgreenhagen.skyblock_chat_notifier.client.config;

import com.tgreenhagen.skyblock_chat_notifier.client.config.gui.TitleTriggerList;
import com.tgreenhagen.skyblock_chat_notifier.client.config.pattern.TriggerPattern;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.StringListBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ModConfigScreenFactory {
    public static Screen create(Screen parent, ModConfig config, Runnable saveCallback) {

        // Create builders
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("title.sbcn.config"));

        ConfigEntryBuilder entryBuilder = ConfigEntryBuilder.create();

        // General config
        ConfigCategory general = builder.getOrCreateCategory(
                Text.translatable("category.sbcn.triggers")
        );

        general.addEntry(
            entryBuilder
                .startBooleanToggle(
                        Text.literal("Enabled"),
                        config.enabled
                )
                .setDefaultValue(true)
                .setTooltip(Text.literal("Toggle entire mod enabled."))
                .setSaveConsumer(value -> config.enabled = value)
                .build()
        );

        // Triggers
        ConfigCategory triggers = builder.getOrCreateCategory(
                Text.translatable("category.sbcn.triggers")
        );

        triggers.addEntry(
            new TitleTriggerList(
                    Text.literal("Title triggers"),
                    config.triggers,
                    config.triggers,
                    value -> config.triggers = value
            )
        );

        builder.setSavingRunnable(saveCallback);

        return builder.build();
    }
}
