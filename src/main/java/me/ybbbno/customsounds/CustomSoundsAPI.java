package me.ybbbno.customsounds;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public final class CustomSoundsAPI {
    private final CustomSounds plugin;

    private CustomSoundsAPI(CustomSounds plugin) { this.plugin = plugin; }

    public VoicechatManager manager() { return plugin.manager(); }

    public static CustomSoundsAPI api() {
        Plugin p = Bukkit.getPluginManager().getPlugin("CustomSounds");
        if (p instanceof CustomSounds) {
            return new CustomSoundsAPI((CustomSounds) p);
        }
        return null;
    }
}
