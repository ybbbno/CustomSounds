package me.ybbbno.customsounds.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import de.maxhenkel.voicechat.api.Position;
import de.maxhenkel.voicechat.api.audiochannel.EntityAudioChannel;
import de.maxhenkel.voicechat.api.audiochannel.LocationalAudioChannel;
import de.maxhenkel.voicechat.api.audiochannel.StaticAudioChannel;
import me.deadybbb.ybmj.BasicManagerHandler;
import me.deadybbb.ybmj.PluginProvider;
import me.ybbbno.customsounds.lavaplayer.types.ContainerAP;
import me.ybbbno.customsounds.lavaplayer.types.EntityAP;
import me.ybbbno.customsounds.lavaplayer.types.LocationalAP;
import me.ybbbno.customsounds.lavaplayer.types.StaticAP;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LavaPlayerManager extends BasicManagerHandler {
    private final AudioPlayerManager manager;

    private final List<EntityAP> entityAPS = new ArrayList<>();
    private final List<LocationalAP> locationalAPS = new ArrayList<>();
    private final List<StaticAP> staticAPS = new ArrayList<>();

    public LavaPlayerManager(PluginProvider plugin) {
        super(plugin);
        manager = new DefaultAudioPlayerManager();
    }

    @Override
    protected void onInit() {
        AudioSourceManagers.registerLocalSource(manager);
    }

    @Override
    protected void onDeinit() {
        destroyAll();
    }

    public boolean createAudioPlayer(String name, LocationalAudioChannel channel) {
        if (getLocationalByName(name) != null) {
            plugin.logger.warning("LocationalAP with name '" + name + "' already exists!");
            return false;
        }
        AudioPlayer player = manager.createPlayer();
        player.addListener(new ListenerAP(plugin, channel, name));
        locationalAPS.add(new LocationalAP(name, channel, player));
        return true;
    }

    public boolean createAudioPlayer(String name, StaticAudioChannel channel) {
        if (getStaticByName(name) != null) {
            plugin.logger.warning("StaticAP with name '" + name + "' already exists!");
            return false;
        }
        AudioPlayer player = manager.createPlayer();
        player.addListener(new ListenerAP(plugin, channel, name));
        staticAPS.add(new StaticAP(name, channel, player));
        return true;
    }

    public boolean createAudioPlayer(String name, EntityAudioChannel channel) {
        if (getLocationalByName(name) != null) {
            plugin.logger.warning("EntityAP with name '" + name + "' already exists!");
            return false;
        }
        AudioPlayer player = manager.createPlayer();
        player.addListener(new ListenerAP(plugin, channel, name));
        entityAPS.add(new EntityAP(name, channel, player));
        return true;
    }

    public List<LocationalAP> getNearLocationalAP(LocationalAP initial) {
        return locationalAPS.stream().filter(ap -> {
            Position initialLoc = initial.channel().getLocation();
            float initialDistance = initial.channel().getDistance();
            Position apLoc = ap.channel().getLocation();
            double distance = Math.sqrt(
                    Math.pow(apLoc.getX() + 0.5 - initialLoc.getX(), 2.0) +
                    Math.pow(apLoc.getY() + 0.5 - initialLoc.getY(), 2.0) +
                    Math.pow(apLoc.getZ() + 0.5 - initialLoc.getZ(), 2.0));

            return distance <= initialDistance;
        }).toList();
    }

    public List<LocationalAP> getNearPosition(Position pos, float distance) {
        return locationalAPS.stream().filter(ap -> {
            Position loc = ap.channel().getLocation();
            double outDistance = Math.sqrt(
                    Math.pow(loc.getX() + 0.5 - pos.getX(), 2.0) +
                    Math.pow(loc.getY() + 0.5 - pos.getY(), 2.0) +
                    Math.pow(loc.getZ() + 0.5 - pos.getZ(), 2.0));

            return outDistance <= distance;
        }).toList();
    }

    public LocationalAP getLocationalByName(String name) {
        return findAudioPlayer(locationalAPS, name).orElse(null);
    }

    public StaticAP getStaticByName(String name) {
        return findAudioPlayer(staticAPS, name).orElse(null);
    }

    public EntityAP getEntityByName(String name) {
        return findAudioPlayer(entityAPS, name).orElse(null);
    }

    public void stopAll() {
        stopAllLocational();
        stopAllStatic();
        stopAllEntity();
    }

    public void stopAllLocational() {
        locationalAPS.forEach(LocationalAP::stop);
    }

    public void stopAllStatic() {
        staticAPS.forEach(StaticAP::stop);
    }

    public void stopAllEntity() {
        entityAPS.forEach(EntityAP::stop);
    }

    public void stopByName(String name) {
        findAudioPlayer(locationalAPS, name).ifPresent(LocationalAP::stop);

        findAudioPlayer(entityAPS, name).ifPresent(EntityAP::stop);

        findAudioPlayer(staticAPS, name).ifPresent(StaticAP::stop);
    }

    public void destroyAll() {
        destroyAllLocational();
        destoryAllStatic();
        destoryAllEntity();
    }

    public void destroyAllLocational() {
        locationalAPS.forEach(LocationalAP::destroy);
        locationalAPS.clear();
    }

    public void destoryAllStatic() {
        staticAPS.forEach(StaticAP::destroy);
        staticAPS.clear();
    }

    public void destoryAllEntity() {
        entityAPS.forEach(EntityAP::destroy);
        entityAPS.clear();
    }

    public void destroyByName(String name) {
        findAudioPlayer(locationalAPS, name).ifPresent(ap -> {
            ap.destroy();
            locationalAPS.remove(ap);
        });

        findAudioPlayer(entityAPS, name).ifPresent(ap -> {
            ap.destroy();
            entityAPS.remove(ap);
        });

        findAudioPlayer(staticAPS, name).ifPresent(ap -> {
            ap.destroy();
            staticAPS.remove(ap);
        });
    }

    public boolean sendData(byte[] data, String name) {
        ContainerAP containerAP = findAudioPlayer(locationalAPS, name).orElse(null);

        if (containerAP == null) {
            containerAP = findAudioPlayer(entityAPS, name).orElse(null);
        }

        if (containerAP == null) {
            containerAP = findAudioPlayer(staticAPS, name).orElse(null);
        }

        if (containerAP != null) {
            containerAP.channel().send(data);
            return true;
        } else {
            plugin.logger.warning("AudioPlayer with name '" + name + "' not found.");
            return false;
        }
    }

    public boolean loadTrack(String identifier, String name, int volume) {
        Optional<AudioPlayer> playerOpt = findAudioPlayer(locationalAPS, name)
                .map(ContainerAP::player);

        if (playerOpt.isEmpty()) {
            playerOpt = findAudioPlayer(entityAPS, name).map(ContainerAP::player);
        }

        if (playerOpt.isEmpty()) {
            playerOpt = findAudioPlayer(staticAPS, name).map(ContainerAP::player);
        }

        if (playerOpt.isPresent()) {
            loadTrack(identifier, playerOpt.get(), volume);
            return true;
        } else {
            plugin.logger.warning("AudioPlayer with name '" + name + "' not found.");
            return false;
        }
    }

    public void loadTrack(String identifier, AudioPlayer player, int volume) {
        player.setVolume(volume);
        manager.loadItem(identifier, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                player.playTrack(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack track = playlist.getSelectedTrack();
                if (track == null) {
                    plugin.logger.warning("No audio found!");
                    return;
                }
                player.playTrack(track);
            }

            @Override
            public void noMatches() {
                plugin.logger.warning("No audio found!");
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                plugin.logger.severe(exception.getMessage());
            }
        });
    }

    public <T extends ContainerAP> Optional<T> findAudioPlayer(List<T> list, String name) {
        return list.stream()
                .filter(ap -> ap.name().equalsIgnoreCase(name))
                .findFirst();
    }
}
