package me.ybbbno.customsounds;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioReference;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import de.maxhenkel.voicechat.api.audiochannel.EntityAudioChannel;
import de.maxhenkel.voicechat.api.audiochannel.LocationalAudioChannel;
import de.maxhenkel.voicechat.api.audiochannel.StaticAudioChannel;
import me.deadybbb.ybmj.BasicManagerHandler;
import me.deadybbb.ybmj.PluginProvider;

public class LavaPlayerManager extends BasicManagerHandler {
    private final AudioPlayerManager manager;

    public LavaPlayerManager(PluginProvider plugin) {
        super(plugin);
        manager = new DefaultAudioPlayerManager();
    }

    @Override
    protected void onInit() {
        AudioSourceManagers.registerLocalSource(manager);
    }

    @Override
    protected void onDeinit() { }

    public AudioPlayer createAudioPlayer(LocationalAudioChannel channel) {
        AudioPlayer player = manager.createPlayer();
        player.addListener(new LocationalPlayerListener(plugin, channel));
        return player;
    }

    public AudioPlayer createAudioPlayer(StaticAudioChannel channel) {
        AudioPlayer player = manager.createPlayer();
        player.addListener(new StaticPlayerListener(plugin, channel));
        return player;
    }

    public AudioPlayer createAudioPlayer(EntityAudioChannel channel) {
        AudioPlayer player = manager.createPlayer();
        player.addListener(new EntityPlayerListener(plugin, channel));
        return player;
    }

    public void loadTrack(String identifier, AudioPlayer player) {
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

}
