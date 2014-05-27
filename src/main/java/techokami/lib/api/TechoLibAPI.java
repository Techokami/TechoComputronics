package techokami.lib.api;

import techokami.lib.api.chat.INicknameHandler;
import techokami.lib.api.chat.INicknameRepository;

public class TechoLibAPI {
	public static TechoLibAPI instance;
	
	public void registerNicknameHandler(INicknameHandler handler) { }
	public INicknameRepository getNicknameRepository() { return null; }
}
