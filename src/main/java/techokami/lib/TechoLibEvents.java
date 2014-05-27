package techokami.lib;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import techokami.lib.block.TileEntityBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.world.WorldEvent;

public class TechoLibEvents {
	@SubscribeEvent
	public void handleTileEntityUnload(WorldEvent.Unload event) {
		for(Object entity: event.world.loadedTileEntityList) {
			if(entity instanceof TileEntityBase) {
				((TileEntityBase)entity).onWorldUnload();
			}
		}
	}
}
