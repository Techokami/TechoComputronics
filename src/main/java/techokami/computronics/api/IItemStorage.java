package techokami.computronics.api;

import net.minecraft.item.ItemStack;
import techokami.computronics.storage.Storage;

public interface IItemStorage {
	public Storage getStorage(ItemStack stack);
	public int getSize(ItemStack stack);
}
