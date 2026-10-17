package me.ybbbno.customsounds.lavaplayer.sstatic;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import de.maxhenkel.voicechat.api.audiochannel.LocationalAudioChannel;
import de.maxhenkel.voicechat.api.audiochannel.StaticAudioChannel;

public class StaticAP {
    private String name;
    private final StaticAudioChannel channel;
    private final AudioPlayer player;

    public StaticAP(String name, StaticAudioChannel channel, AudioPlayer player) {
        this.name = name;
        this.channel = channel;
        this.player = player;
    }

    public String name() { return name; }
    public void name(String name) { this.name = name; }
    public StaticAudioChannel channel() { return channel; }
    public AudioPlayer player() { return player; }
}
