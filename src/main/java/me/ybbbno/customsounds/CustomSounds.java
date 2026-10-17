package me.ybbbno.customsounds;

import de.maxhenkel.voicechat.api.BukkitVoicechatService;
import me.deadybbb.ybmj.PluginProvider;

public final class CustomSounds extends PluginProvider {
    private BukkitVoicechatService service;
    private VoicechatManager manager;

    @Override
    public void onEnable() {
        service = getServer().getServicesManager().load(BukkitVoicechatService.class);
        if (service != null) {
            manager = new VoicechatManager(this);
            service.registerPlugin(manager);
            registerCommand("customsounds", new CustomSoundsCommand(this, manager));
        } else {
            logger.severe("Simple Voice Chat is not installed!");
        }
    }

    @Override
    public void onDisable() {
        if (service != null) {
            manager.deinit();
        }
    }

    public VoicechatManager manager() { return manager; }
}
