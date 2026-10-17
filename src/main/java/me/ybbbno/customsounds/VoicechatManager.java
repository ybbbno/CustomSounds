package me.ybbbno.customsounds;

import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import de.maxhenkel.voicechat.api.audiochannel.EntityAudioChannel;
import de.maxhenkel.voicechat.api.audiochannel.LocationalAudioChannel;
import de.maxhenkel.voicechat.api.audiochannel.StaticAudioChannel;
import de.maxhenkel.voicechat.api.events.*;
import de.maxhenkel.voicechat.api.packets.MicrophonePacket;
import me.deadybbb.ybmj.LegacyTextHandler;
import me.deadybbb.ybmj.PluginProvider;
import me.ybbbno.customsounds.lavaplayer.LavaPlayerManager;
import me.ybbbno.customsounds.lavaplayer.types.EntityAP;
import me.ybbbno.customsounds.lavaplayer.types.LocationalAP;
import me.ybbbno.customsounds.lavaplayer.types.StaticAP;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VoicechatManager extends LavaPlayerManager implements VoicechatPlugin {
    private final PluginProvider plugin;
    private VoicechatServerApi api;

    private final List<VoicechatLifecycleListener> listeners = new ArrayList<>();

    public VoicechatManager(PluginProvider plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public String getPluginId() {
        return "CustomSounds";
    }

    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(VoicechatServerStartedEvent.class, this::onInit);
        registration.registerEvent(VoicechatServerStoppedEvent.class, this::onDeinit);
        registration.registerEvent(MicrophonePacketEvent.class, this::onMicrophone);
        registration.registerEvent(LocationalSoundPacketEvent.class, this::onLocational);
    }

    private void onInit(VoicechatServerStartedEvent event) {
        api = event.getVoicechat();
        init();
        listeners.forEach(l -> l.onVoicechatServerStarted(event));
    }

    private void onDeinit(VoicechatServerStoppedEvent event) {
        listeners.forEach(l -> l.onVoicechatServerStopped(event));
        deinit();
    }

    private void onMicrophone(MicrophonePacketEvent event) {
        listeners.forEach(l -> l.onMicrophonePacket(event));
    }

    private void onLocational(LocationalSoundPacketEvent event) { listeners.forEach(l -> l.onLocationalPacketEvent(event)); }

    public void addListener(VoicechatLifecycleListener listener) {
        listeners.add(listener);
    }

    public void removeListener(VoicechatLifecycleListener listener) {
        listeners.remove(listener);
    }

    public boolean createAudioPlayer(Entity entity) {
        EntityAudioChannel channel = createEntityChannel(entity);
        if (channel == null) {
            plugin.logger.severe("Failed to create EntityAudioChannel for entity: " + entity);
            return false;
        }
        return createAudioPlayer(String.valueOf(entity.getEntityId()), channel);
    }

    public boolean createAudioPlayer(Entity entity, float distance) {
        EntityAudioChannel channel = createEntityChannel(entity);
        if (channel == null) {
            plugin.logger.severe("Failed to create EntityAudioChannel for entity: " + entity);
            return false;
        }
        channel.setDistance(distance);
        return createAudioPlayer(String.valueOf(entity.getEntityId()), channel);
    }

    public boolean createAudioPlayer(Entity entity, String volume_category) {
        EntityAudioChannel channel = createEntityChannel(entity);
        if (channel == null) {
            plugin.logger.severe("Failed to create EntityAudioChannel for entity: " + entity);
            return false;
        }
        channel.setCategory(volume_category);
        return createAudioPlayer(String.valueOf(entity.getEntityId()), channel);
    }

    public boolean createAudioPlayer(Entity entity, float distance, String volume_category) {
        EntityAudioChannel channel = createEntityChannel(entity);
        if (channel == null) {
            plugin.logger.severe("Failed to create EntityAudioChannel for entity: " + entity);
            return false;
        }
        channel.setDistance(distance);
        channel.setCategory(volume_category);
        return createAudioPlayer(String.valueOf(entity.getEntityId()), channel);
    }

    public boolean createAudioPlayer(String name, Entity entity) {
        EntityAudioChannel channel = createEntityChannel(entity);
        if (channel == null) {
            plugin.logger.severe("Failed to create EntityAudioChannel for entity: " + entity);
            return false;
        }
        return createAudioPlayer(name, channel);
    }

    public boolean createAudioPlayer(String name, Entity entity, int distance) {
        EntityAudioChannel channel = createEntityChannel(entity);
        if (channel == null) {
            plugin.logger.severe("Failed to create EntityAudioChannel for entity: " + entity);
            return false;
        }
        channel.setDistance(distance);
        return createAudioPlayer(name, channel);
    }

    public boolean createAudioPlayer(String name, Entity entity, String volume_category) {
        EntityAudioChannel channel = createEntityChannel(entity);
        if (channel == null) {
            plugin.logger.severe("Failed to create EntityAudioChannel for entity: " + entity);
            return false;
        }
        channel.setCategory(volume_category);
        return createAudioPlayer(name, channel);
    }

    public boolean createAudioPlayer(String name, Entity entity, int distance, String volume_category) {
        EntityAudioChannel channel = createEntityChannel(entity);
        if (channel == null) {
            plugin.logger.severe("Failed to create EntityAudioChannel for entity: " + entity);
            return false;
        }
        channel.setDistance(distance);
        channel.setCategory(volume_category);
        return createAudioPlayer(name, channel);
    }

    public boolean createAudioPlayer(Location location, float distance) {
        LocationalAudioChannel channel = createLocationalChannel(location, distance);
        if (channel == null) {
            plugin.logger.severe("Failed to create LocationalAudioChannel for location: " + location);
            return false;
        }
        return createAudioPlayer(LegacyTextHandler.formatLocation(location), channel);
    }

    public boolean createAudioPlayer(String name, Location location, float distance) {
        LocationalAudioChannel channel = createLocationalChannel(location, distance);
        if (channel == null) {
            plugin.logger.severe("Failed to create LocationalAudioChannel for location: " + location);
            return false;
        }
        return createAudioPlayer(name, channel);
    }

    public boolean createAudioPlayer(Location location, float distance, String volume_category) {
        LocationalAudioChannel channel = createLocationalChannel(location, distance);
        if (channel == null) {
            plugin.logger.severe("Failed to create LocationalAudioChannel for location: " + location);
            return false;
        }
        channel.setCategory(volume_category);
        return createAudioPlayer(LegacyTextHandler.formatLocation(location), channel);
    }


    public boolean createAudioPlayer(String name, Location location, float distance, String volume_category) {
        LocationalAudioChannel channel = createLocationalChannel(location, distance);
        if (channel == null) {
            plugin.logger.severe("Failed to create LocationalAudioChannel for location: " + location);
            return false;
        }
        channel.setCategory(volume_category);
        return createAudioPlayer(name, channel);
    }

    public boolean createAudioPlayer(String name) {
        StaticAudioChannel channel = createStaticChannel();
        if (channel == null) {
            plugin.logger.severe("Failed to create StaticAudioChannel");
            return false;
        }
        return createAudioPlayer(name, channel);
    }

    public boolean createAudioPlayer(Player player) {
        return createAudioPlayer("static-"+player.getName(), player);
    }

    public boolean createAudioPlayer(Player player, String volume_category) {
        return createAudioPlayer("static-"+player.getName(), player, volume_category);
    }

    public boolean createAudioPlayer(String name, Player player) {
        StaticAudioChannel channel = createStaticChannel();
        if (channel == null) {
            plugin.logger.severe("Failed to create StaticAudioChannel");
            return false;
        }
        channel.setBypassGroupIsolation(true);
        channel.addTarget(api.getConnectionOf(player.getUniqueId()));
        return createAudioPlayer(name, channel);
    }

    public boolean createAudioPlayer(String name, Player player, String volume_category) {
        StaticAudioChannel channel = createStaticChannel();
        if (channel == null) {
            plugin.logger.severe("Failed to create StaticAudioChannel");
            return false;
        }
        channel.setBypassGroupIsolation(true);
        channel.addTarget(api.getConnectionOf(player.getUniqueId()));
        channel.setCategory(volume_category);
        return createAudioPlayer(name, channel);
    }

    public boolean createAudioPlayer(String name, String volume_category) {
        StaticAudioChannel channel = createStaticChannel();
        if (channel == null) {
            plugin.logger.severe("Failed to create StaticAudioChannel");
            return false;
        }
        channel.setCategory(volume_category);
        return createAudioPlayer(name, channel);
    }

    public boolean sendData(@NotNull MicrophonePacket packet, String name) {
        return sendData(packet.getOpusEncodedData(), name);
    }

    public boolean sendData(@NotNull MicrophonePacket packet, Location loc) {
        return sendData(packet.getOpusEncodedData(), LegacyTextHandler.formatLocation(loc));
    }

    public boolean sendData(@NotNull MicrophonePacket packet, @NotNull Entity entity) {
        return sendData(packet.getOpusEncodedData(), String.valueOf(entity.getEntityId()));
    }

    public boolean sendData(byte[] data, Location loc) {
        return sendData(data, LegacyTextHandler.formatLocation(loc));
    }

    public boolean sendData(byte[] data, @NotNull Entity entity) {
        return sendData(data, String.valueOf(entity.getEntityId()));
    }

    public boolean loadTrack(String identifier, Player player, int volume) {
        return loadTrack(identifier, "static-"+player.getName(), volume);
    }

    public boolean loadTrack(String identifier, Location loc, int volume) {
        return loadTrack(identifier, LegacyTextHandler.formatLocation(loc), volume);
    }

    public boolean loadTrack(String identifier, @NotNull Entity entity, int volume) {
        return loadTrack(identifier, String.valueOf(entity.getEntityId()), volume);
    }

    public LocationalAP getLocational(Location location) {
        return getLocationalByName(LegacyTextHandler.formatLocation(location));
    }

    public StaticAP getPlayer(Player player) {
        return getStaticByName("static-"+player.getName());
    }

    public EntityAP getEntity(@NotNull Entity entity) {
        return getEntityByName(String.valueOf(entity.getEntityId()));
    }

    public void stopByLocation(Location location) {
        stopByName(LegacyTextHandler.formatLocation(location));
    }

    public void stopByPlayer(Player player) {
        stopByName("static-"+player.getName());
    }

    public void stopByEntity(@NotNull Entity entity) {
        stopByName(String.valueOf(entity.getEntityId()));
    }

    public void destroyByLocation(Location location) {
        destroyByName(LegacyTextHandler.formatLocation(location));
    }

    public void destroyByEntity(@NotNull Entity entity) {
        destroyByName(String.valueOf(entity.getEntityId()));
    }

    private EntityAudioChannel createEntityChannel(Entity entity) {
        UUID uuid = UUID.randomUUID();
        return api.createEntityAudioChannel(uuid, api.fromEntity(entity));
    }

    private LocationalAudioChannel createLocationalChannel(@NotNull Location location, float distance) {
        UUID uuid = UUID.randomUUID();
        LocationalAudioChannel channel = api.createLocationalAudioChannel(uuid,
                api.fromServerLevel(location.getWorld()),
                api.createPosition(location.getX() + 0.5, location.getY() + 0.5, location.getZ() + 0.5)
        );
        if (channel == null) return null;
        channel.setDistance(distance);
        return channel;
    }

    private StaticAudioChannel createStaticChannel() {
        UUID uuid = UUID.randomUUID();
        return api.createStaticAudioChannel(uuid);
    }
}
