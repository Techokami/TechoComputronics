package techokami.computronics.audio;

import java.util.HashMap;

import techokami.computronics.Computronics;
import techokami.lib.audio.StreamingAudioPlayer;
import techokami.lib.audio.StreamingPlaybackManager;

public class DFPWMPlaybackManager extends StreamingPlaybackManager {
	public DFPWMPlaybackManager(boolean isClient) {
		super(isClient);
	}

	public StreamingAudioPlayer create() {
		return new StreamingAudioPlayer(32768, false, false, (int)Math.round(Computronics.BUFFER_MS / 250));
	}
}
