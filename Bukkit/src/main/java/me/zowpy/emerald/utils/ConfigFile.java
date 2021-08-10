package me.zowpy.emerald.utils;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

/**
 * This Project is property of Zowpy © 2021
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 8/10/2021
 * Project: Emerald
 */

@Getter
public class ConfigFile {

    private File file;
    private YamlConfiguration config;

    @SneakyThrows
    public ConfigFile(Plugin plugin, String name) {
        file = new File(plugin.getDataFolder(), name + ".yml");

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        if (!file.exists()) {
            file.createNewFile();
            plugin.saveResource(name + ".yml", true);
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    @SneakyThrows
    public void save() {
        config.save(file);
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }
}
