package techokami.lib.chat;

import techokami.lib.TechoLibMod;
import techokami.lib.Packets;
import techokami.lib.api.chat.INicknameHandler;
import techokami.lib.network.Packet;
import techokami.lib.util.PlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerConnectionFromClientEvent;

public class NicknameNetworkHandler implements INicknameHandler {
	
	@SubscribeEvent
	public void playerLoggedIn(PlayerLoggedInEvent event) {
		if(event.player instanceof EntityPlayer)
			TechoLibMod.nick.updateNickname(((EntityPlayer)event.player).getCommandSenderName());
		
		for(Object o: MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
			if(o == null || !(o instanceof EntityPlayer)) continue;
			EntityPlayer e = (EntityPlayer)o;
			String username = e.getCommandSenderName();
			String nickname = TechoLibMod.nick.getNickname(username);
			if(!nickname.equals(username))
				sendNicknamePacket(username, nickname, event.player);
		}
	}
	
	private void sendNicknamePacket(String realname, String nickname, EntityPlayer target) {
		try {
			Packet packet = TechoLibMod.packet.create(Packets.NICKNAME_CHANGE)
					.writeString(realname)
					.writeString(nickname);
			if(target == null)
				TechoLibMod.packet.sendToAll(packet);
			else
				TechoLibMod.packet.sendTo(packet, (EntityPlayerMP)target);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onNicknameUpdate(String realname, String nickname) {
		sendNicknamePacket(realname, nickname, null);
	}
}
