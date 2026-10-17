package me.ybbbno.customsounds;

import de.maxhenkel.voicechat.api.events.LocationalSoundPacketEvent;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import de.maxhenkel.voicechat.api.events.VoicechatServerStartedEvent;
import de.maxhenkel.voicechat.api.events.VoicechatServerStoppedEvent;

public interface VoicechatLifecycleListener {
    default void onVoicechatServerStarted(VoicechatServerStartedEvent event) {}
    default void onVoicechatServerStopped(VoicechatServerStoppedEvent event) {}
    default void onMicrophonePacket(MicrophonePacketEvent event) {}
    default void onLocationalPacketEvent(LocationalSoundPacketEvent event) {}
}
