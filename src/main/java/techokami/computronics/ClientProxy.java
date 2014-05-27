package techokami.computronics;

import techokami.computronics.gui.GuiCipherBlock;
import techokami.computronics.gui.GuiOneSlot;
import techokami.computronics.gui.GuiTapePlayer;
import techokami.computronics.tile.ContainerCipherBlock;
import techokami.computronics.tile.ContainerTapeReader;
import techokami.lib.gui.GuiHandler;

public class ClientProxy extends CommonProxy {
	public boolean isClient() { return true; }
	
	public void registerGuis(GuiHandler gui) {
		gui.registerGui(ContainerTapeReader.class, GuiTapePlayer.class);
		gui.registerGui(ContainerCipherBlock.class, GuiCipherBlock.class);
	}
}
