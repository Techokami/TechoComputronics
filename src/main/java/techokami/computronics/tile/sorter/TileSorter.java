package techokami.computronics.tile.sorter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import buildcraft.api.transport.IPipeConnection;
import buildcraft.api.transport.IPipeTile;
import buildcraft.api.transport.IPipeTile.PipeType;
import buildcraft.api.transport.PipeWire;
import li.cil.oc.api.network.Arguments;
import li.cil.oc.api.network.Callback;
import li.cil.oc.api.network.Context;
import li.cil.oc.api.network.SimpleComponent;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import techokami.computronics.api.ISortingOutputHandler;
import techokami.lib.block.TileEntityBase;

@Optional.InterfaceList({
	@Optional.Interface(iface = "buildcraft.api.transport.IPipeTile", modid = "BuildCraft|Core"),
	@Optional.Interface(iface = "buildcraft.api.transport.IPipeConnection", modid = "BuildCraft|Core"),
	@Optional.Interface(iface = "li.cil.li.oc.network.SimpleComponent", modid = "OpenComputers")
})
public class TileSorter extends TileEntityBase implements SimpleComponent, IPipeTile, IPipeConnection {
	private IInventory inventory = null;
	private ForgeDirection inputSide;
	private Mode mode = Mode.MANUAL;
	
	private TileEntity input, output;
	private ISortingOutputHandler outputHandler;
	
	private ArrayList<ISortingOutputHandler> outputHandlerList = new ArrayList<ISortingOutputHandler>();
	
	public TileSorter() {
		if(Loader.isModLoaded("BuildCraft|Core")) addSortingHandler(new SortingHandlerBC());
	}
	
	public void addSortingHandler(Object sortingHandler) {
		if(sortingHandler instanceof ISortingOutputHandler)
			outputHandlerList.add((ISortingOutputHandler)sortingHandler);
	}
	
	public enum Mode {
		MANUAL,
		AUTOMATIC
	};
	
	// Portable functions
	
	public int getInventorySize() {
		return inventory != null ? inventory.getSizeInventory() : 0;
	}
	
	public ItemStack getInventoryStack(int slot) {
		if(slot < 0 || slot >= getInventorySize()) return null;
		ItemStack stack = inventory.getStackInSlot(slot);
		if(inventory instanceof ISidedInventory) {
			ISidedInventory sided = (ISidedInventory)inventory;
			// Sided inventory checks
			if(!sided.canExtractItem(slot, stack, inputSide.ordinal())) return null;
		}
		if(stack == null || stack.getItem() == null || stack.stackSize == 0) stack = null;
		return stack;
	}
	
	public Map<String, Object> getInventorySlot(int slot) {
		ItemStack stack = getInventoryStack(slot);
		if(stack == null) return null;
		
		HashMap<String, Object> data = new HashMap<String, Object>();
		
		data.put("size", stack.stackSize);
		data.put("item", stack.getUnlocalizedName());
		data.put("damage", stack.getItemDamage());
		return data;
	}
	
	public boolean pull(int slot) {
		if(output == null || inventory == null) return false;
		ItemStack stack = getInventoryStack(slot);
		if(stack == null) return false;
		
		int amount = outputHandler.output(output, stack.copy(), false);
		if(amount > 0) inventory.decrStackSize(slot, amount);
		
		return amount > 0;
	}
	
	// Internal handlery
	
	// BuildCraft - input
	
	@Optional.Method(modid = "BuildCraft|Core")
	public boolean findInventoryBC(TileEntity te, ForgeDirection side) {
		if(te instanceof IPipeTile && ((IPipeTile)te).getPipeType() == PipeType.ITEM) {
			this.input = te;
			this.inputSide = side;
			return true;
		}
		return false;
	}
	
	@Override
	@Optional.Method(modid = "BuildCraft|Core")
	public PipeType getPipeType() {
		return PipeType.ITEM;
	}

	@Override
	@Optional.Method(modid = "BuildCraft|Core")
	public int injectItem(ItemStack stack, boolean doAdd, ForgeDirection from) {
		if(mode != Mode.AUTOMATIC || from == ForgeDirection.UP || from == ForgeDirection.DOWN) return 0;
		return outputHandler.output(output, stack, !doAdd);
	}

	@Override
	@Optional.Method(modid = "BuildCraft|Core")
	public boolean isPipeConnected(ForgeDirection with) {
		return (with != ForgeDirection.DOWN);
	}

	@Override
	@Optional.Method(modid = "BuildCraft|Core")
	public boolean isWireActive(PipeWire wire) {
		return false;
	}

	@Override
	@Optional.Method(modid = "BuildCraft|Core")
	public ConnectOverride overridePipeConnection(PipeType type,
			ForgeDirection with) {
		return ((with != ForgeDirection.DOWN) && (type == PipeType.ITEM)) ? ConnectOverride.CONNECT : ConnectOverride.DISCONNECT;
	}
	
	// Vanilla - input
	
	private boolean findInventory() {
		for(int i = 2; i < 6; i++) {
			ForgeDirection side = ForgeDirection.getOrientation(i);
			Block block = worldObj.getBlock(xCoord + side.offsetX, yCoord + side.offsetY, zCoord + side.offsetZ);
			int meta = worldObj.getBlockMetadata(xCoord + side.offsetX, yCoord + side.offsetY, zCoord + side.offsetZ);
			if(block.hasTileEntity(meta)) {
				TileEntity te = worldObj.getTileEntity(xCoord + side.offsetX, yCoord + side.offsetY, zCoord + side.offsetZ);
				// Check for blocked mods
				if(te.getClass().getName().startsWith("li.cil.oc")) continue;
				
				// Check for known types
				if(Loader.isModLoaded("BuildCraft|Core")) {
					if(findInventoryBC(te, side)) return true;
				}
				if(te instanceof IInventory) {
					this.inventory = (IInventory)te;
					this.input = te;
					this.inputSide = side;
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean findOutput() {
		int yPos = yCoord + 1;
		Block block = worldObj.getBlock(xCoord, yPos, zCoord);
		int meta = worldObj.getBlockMetadata(xCoord, yPos, zCoord);
		if(block.hasTileEntity(meta)) {
			TileEntity tile = worldObj.getTileEntity(xCoord, yPos, zCoord);
			for(ISortingOutputHandler handler: outputHandlerList)
				if(handler.isOutputtable(tile)) {
					this.outputHandler = handler;
					this.output = tile;
					return true;
				}
		}
		return false;
	}
	
	@Override
	public boolean canUpdate() { return false; }
	
	public void update() {
		this.inventory = null;
		this.input = null;
		this.output = null;
		
		boolean foundInventory = findInventory();
		boolean foundOutput = findOutput();
		this.mode = this.inventory != null ? Mode.MANUAL : Mode.AUTOMATIC;
		
		int newMeta = (foundInventory || foundOutput) ? 8 : 0;
		int oldMeta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord) & 7;
		try {
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, oldMeta | newMeta, 2);
		} catch(Exception e) {
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}

	// OpenComputers
	
	@Override
	@Optional.Method(modid="OpenComputers")
	public String getComponentName() {
		// TODO Auto-generated method stub
		return "sorter";
	}
	
	@Callback(direct = true)
	@Optional.Method(modid="OpenComputers")
	public Object[] getInventorySize(Context ctx, Arguments args) {
		return new Object[]{getInventorySize()};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid="OpenComputers")
	public Object[] getInventoryStack(Context ctx, Arguments args) {
		if(args.count() < 1 || !args.isInteger(0)) return null;
		return new Object[]{getInventorySlot(args.checkInteger(0))};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid="OpenComputers")
	public Object[] pull(Context ctx, Arguments args) {
		if(args.count() < 1 || !args.isInteger(0)) return null;
		return new Object[]{pull(args.checkInteger(0))};
	}
	
	@Override
	public void validate() {
		super.validate();
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
}
