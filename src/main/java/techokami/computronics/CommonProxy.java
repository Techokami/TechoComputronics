package techokami.computronics;

import techokami.computronics.gui.GuiCipherBlock;
import techokami.computronics.tile.ContainerCipherBlock;
import techokami.computronics.tile.ContainerTapeReader;
import techokami.lib.gui.GuiHandler;

public class CommonProxy {
	public boolean isClient() { return false; }

	public void registerGuis(GuiHandler gui) {
		gui.registerGui(ContainerTapeReader.class, null);
		gui.registerGui(ContainerCipherBlock.class, null);
	}
}
