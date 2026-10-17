package me.ybbbno.customsounds.lavaplayer.entity;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import de.maxhenkel.voicechat.api.audiochannel.EntityAudioChannel;

public class EntityAP {
    private String name;
    private final EntityAudioChannel channel;
    private final AudioPlayer player;

    public EntityAP(String name, EntityAudioChannel channel, AudioPlayer player) {
        this.name = name;
        this.channel = channel;
        this.player = player;
    }

    public String name() { return name; }
    public void name(String name) { this.name = name; }
    public EntityAudioChannel channel() { return channel; }
    public AudioPlayer player() { return player; }
}
