package me.ybbbno.customsounds.lavaplayer.types;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import de.maxhenkel.voicechat.api.audiochannel.AudioChannel;

public interface ContainerAP {
    String name();
    AudioPlayer player();
    AudioChannel channel();
    void stop();
    void destroy();
}
