package techokami.computronics;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import techokami.computronics.storage.StorageManager;
import techokami.computronics.tile.TileChatBox;
import net.minecraft.entity.player.EntityPlayerMP;
//import net.minecraft.network.packet.NetHandler;
//import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.ChatMessageComponent;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.world.WorldEvent;

public class ComputronicsEventHandler {
	@SubscribeEvent
	public void chatEvent(ServerChatEvent event) {
		for(Object o: event.player.worldObj.loadedTileEntityList) {
			if(o instanceof TileChatBox) {
				TileChatBox te = (TileChatBox)o;
				if(te.isCreative() || event.player.getDistance(te.xCoord, te.yCoord, te.zCoord) < te.getDistance()) {
					te.receiveChatMessage(event);
				}
			}
		}
	}
	
/*	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onSound(SoundLoadEvent event) {
		event.manager.addSound("computronics:tape_eject.ogg");
		event.manager.addSound("computronics:tape_rewind.ogg");
		event.manager.addSound("computronics:tape_insert.ogg");
		event.manager.addSound("computronics:tape_button.ogg");
	}

	@Override
	public Packet3Chat serverChat(NetHandler handler, Packet3Chat message) {
		if(!(message.message.startsWith("/"))) return message;
		
		for(Object o: handler.getPlayer().worldObj.loadedTileEntityList) {
			if(o instanceof TileChatBox) {
				TileChatBox te = (TileChatBox)o;
				if(te.isCreative() || (handler.getPlayer().getDistance(te.xCoord, te.yCoord, te.zCoord) < Computronics.CHATBOX_DISTANCE && message.message.startsWith("/me") && Computronics.CHATBOX_ME_DETECT)) {
					te.receiveChatMessage(new ServerChatEvent((EntityPlayerMP)handler.getPlayer(), message.message, ChatMessageComponent.createFromText(message.message)));
				}
			}
		}
		return message;
	}

	@Override
	public Packet3Chat clientChat(NetHandler handler, Packet3Chat message) {
		return message;
	} */
}
