package me.ybbbno.customsounds.lavaplayer.types;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import de.maxhenkel.voicechat.api.audiochannel.EntityAudioChannel;

public record EntityAP(
        String name,
        EntityAudioChannel channel,
        AudioPlayer player
) implements ContainerAP {

    @Override
    public void stop() {
        player.stopTrack();
        channel.flush();
    }

    @Override
    public void destroy() {
        player.destroy();
        channel.flush();
    }
}