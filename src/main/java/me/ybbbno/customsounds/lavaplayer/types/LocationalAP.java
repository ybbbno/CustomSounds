package me.ybbbno.customsounds.lavaplayer.locational;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import de.maxhenkel.voicechat.api.audiochannel.LocationalAudioChannel;

public class LocationalAP {
    private String name;
    private final LocationalAudioChannel channel;
    private final AudioPlayer player;

    public LocationalAP(String name, LocationalAudioChannel channel, AudioPlayer player) {
        this.name = name;
        this.channel = channel;
        this.player = player;
    }

    public String name() { return name; }
    public void name(String name) { this.name = name; }
    public LocationalAudioChannel channel() { return channel; }
    public AudioPlayer player() { return player; }
}
