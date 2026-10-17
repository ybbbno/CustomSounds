package me.ybbbno.customsounds.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackState;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import de.maxhenkel.voicechat.api.audiochannel.AudioChannel;
import de.maxhenkel.voicechat.api.audiochannel.LocationalAudioChannel;
import me.deadybbb.ybmj.PluginProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class APListener extends AudioEventAdapter {
    private final PluginProvider plugin;
    private final AudioChannel channel;

    public APListener(PluginProvider plugin, AudioChannel channel) {
        this.plugin = plugin;
        this.channel = channel;
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        new Thread(() -> {
            long start = 0L;
            try {
                while(!channel.isClosed() && player.getPlayingTrack() != null && !Thread.currentThread().isInterrupted()) {
                    if (track.getState() == AudioTrackState.FINISHED) break;

                    AudioFrame frame;
                    if (Objects.isNull(frame = player.provide(5L, TimeUnit.MILLISECONDS))) continue;

                    channel.send(frame.getData());

                    if (start == 0L) start = System.currentTimeMillis();
                    long wait = (start + frame.getTimecode()) - System.currentTimeMillis();
                    if (wait > 0) {
                        TimeUnit.MILLISECONDS.sleep(wait);
                    };
                }
            } catch (InterruptedException e) {
                plugin.logger.warning("Track "+track.getIdentifier()+" got InterruptedException its stop most likely");
                Thread.currentThread().interrupt();
            } catch (Throwable e) {
                plugin.logger.severe("Track "+track.getIdentifier()+" got Throwable Exception: "+ e.getMessage());
            }
        }).start();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        channel.flush();
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        plugin.logger.severe(exception.getMessage());
    }
}
