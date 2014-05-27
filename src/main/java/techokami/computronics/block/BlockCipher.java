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
import techokami.computronics.tile.TileCipherBlock;
import techokami.computronics.tile.TileIronNote;
import techokami.computronics.tile.TileTapeDrive;
import techokami.lib.block.BlockBase;

public class BlockCipher extends BlockMachineSidedIcon {
	private IIcon mFront;
	
	public BlockCipher() {
		super();
		this.setBlockName("computronics.cipher");
		this.setGuiID(1);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileCipherBlock();
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
		mFront = r.registerIcon("computronics:cipher_front");
	}
}
