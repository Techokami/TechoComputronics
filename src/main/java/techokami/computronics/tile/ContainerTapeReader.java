package techokami.computronics.tile;

import net.minecraft.entity.player.InventoryPlayer;
import techokami.computronics.Computronics;
import techokami.lib.block.ContainerBase;
import techokami.lib.block.TileEntityInventory;
import techokami.lib.util.SlotTyped;

public class ContainerTapeReader extends ContainerBase {

	public ContainerTapeReader(TileEntityInventory entity,
			InventoryPlayer inventoryPlayer) {
		super(entity, inventoryPlayer);
		this.addSlotToContainer(new SlotTyped(entity, 0, 80, 34, new Object[]{Computronics.instance.itemTape}));
		this.bindPlayerInventory(inventoryPlayer, 8, 84);
	}

}
