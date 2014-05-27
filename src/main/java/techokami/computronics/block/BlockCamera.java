package techokami.computronics.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import techokami.computronics.Computronics;
import techokami.computronics.tile.TileCamera;
import techokami.lib.block.BlockBase;

public class BlockCamera extends BlockMachineSidedIcon {
	private IIcon mFront;
	
	public BlockCamera() {
		super();
		this.setBlockName("computronics.camera");
		this.setRotation(Rotation.SIX);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileCamera();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getAbsoluteSideIcon(int sideNumber, int metadata) {
		return sideNumber == 2 ? mFront : super.getAbsoluteSideIcon(sideNumber, metadata);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister r) {
		super.registerBlockIcons(r);
		mFront = r.registerIcon("computronics:camera_front");
	}
	
	@Override
	public boolean emitsRedstone(IBlockAccess world, int x, int y, int z) {
		return Computronics.CAMERA_REDSTONE_REFRESH;
	}
}
