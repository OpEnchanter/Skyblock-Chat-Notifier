package com.tgreenhagen.skyblock_chat_notifier.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Handles loading and saving the mod's configuration JSON.
 */
public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_NAME = "sbcn.json"; // use your mod id

    private static ModConfig config;

    /** Returns the config file in .minecraft/config/ */
    private static File getConfigFile() {
        File configDir = FabricLoader.getInstance().getConfigDir().toFile();
        return new File(configDir, FILE_NAME);
    }

    /** Load config from disk (or create default if missing). Call this once on startup. */
    public static void load() {
        File file = getConfigFile();

        if (!file.exists()) {
            // First time: use defaults and save a new file
            config = new ModConfig();
            save();
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            config = GSON.fromJson(reader, ModConfig.class);
            if (config == null) {
                config = new ModConfig();
            }
        } catch (IOException e) {
            e.printStackTrace();
            // On error, fall back to defaults (so mod still runs)
            config = new ModConfig();
        }
    }

    /** Save current config to disk. Call when settings change. */
    public static void save() {
        File file = getConfigFile();
        // Ensure config directory exists
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            //noinspection ResultOfMethodCallIgnored
            parent.mkdirs();
        }

        try (FileWriter writer = new FileWriter(file)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Get the current config instance (never null after load()). */
    public static ModConfig get() {
        if (config == null) {
            // Safety fallback; ideally load() is called first
            config = new ModConfig();
        }
        return config;
    }
}