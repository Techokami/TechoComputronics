package techokami.lib.audio;

import java.util.HashMap;

import techokami.lib.audio.StreamingAudioPlayer;

public abstract class StreamingPlaybackManager {
	private final boolean isClient;
	
	public StreamingPlaybackManager(boolean isClient) {
		this.isClient = isClient;
	}
	
	private int currentId = 0;
	private HashMap<Integer, StreamingAudioPlayer> players = new HashMap<Integer, StreamingAudioPlayer>();
	
	public abstract StreamingAudioPlayer create();
	
	public int newPlayer() {
		StreamingAudioPlayer codec = create();
		players.put(currentId++, codec);
		return currentId-1;
	}
	
	public void removePlayer(int id) {
		if(players.containsKey(id)) {
			players.get(id).stop();
			players.remove(id);
		}
	}
	
	public StreamingAudioPlayer getPlayer(int id) {
		if(!players.containsKey(id)) players.put(id, create());
		return players.get(id);
	}
}
