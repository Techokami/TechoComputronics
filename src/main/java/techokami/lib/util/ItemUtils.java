package techokami.lib.util;

import techokami.lib.TechoLibMod;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ItemUtils {
    public static void dropItems(World world, int x, int y, int z, IInventory inventory) {
    	for (int i = 0; i < inventory.getSizeInventory(); i++) {
    		ItemStack item = inventory.getStackInSlot(i);

    		if (item != null && item.stackSize > 0) {
    			inventory.setInventorySlotContents(i, null);
    			dropItem(world, x, y, z, item);
    			item.stackSize = 0;
    		}
    	}
    }
    
    public static void dropItems(World world, int x, int y, int z) {
    	TileEntity tileEntity = world.getTileEntity(x, y, z);
    	if (tileEntity == null || !(tileEntity instanceof IInventory)) {
    		return;
    	}
    	IInventory inventory = (IInventory) tileEntity;
    	dropItems(world, x, y, z, inventory);
    }

	public static void dropItem(World world, int x, int y, int z, ItemStack item) {
		float rx = TechoLibMod.rand.nextFloat() * 0.8F + 0.1F;
		float ry = TechoLibMod.rand.nextFloat() * 0.8F + 0.1F;
		float rz = TechoLibMod.rand.nextFloat() * 0.8F + 0.1F;

		EntityItem entityItem = new EntityItem(world,
				x + rx, y + ry, z + rz,
				new ItemStack(item.getItem(), item.stackSize, item.getItemDamage()));

		if (item.hasTagCompound()) {
			entityItem.getEntityItem().setTagCompound((NBTTagCompound)item.getTagCompound().copy());
		}

		float factor = 0.05F;
		entityItem.motionX = TechoLibMod.rand.nextGaussian() * factor;
		entityItem.motionY = TechoLibMod.rand.nextGaussian() * factor + 0.2F;
		entityItem.motionZ = TechoLibMod.rand.nextGaussian() * factor;
		world.spawnEntityInWorld(entityItem);
	}
}
