package techokami.lib;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import techokami.lib.network.MessageHandlerBase;
import techokami.lib.network.Packet;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

public class NetworkHandlerClient extends MessageHandlerBase {
	@Override
	public void onMessage(Packet packet, INetHandler handler, EntityPlayer player, int command)
			throws IOException {
		switch(command) {
			case Packets.NICKNAME_CHANGE:
				String username = packet.readString();
				String nickname = packet.readString();
				TechoLibMod.nick.setNickname(username, nickname);
				break;
			case Packets.NANO_NANO:
				TechoLibMod.keyClient.scheduleSpin();
				break;
		}
	}
}
