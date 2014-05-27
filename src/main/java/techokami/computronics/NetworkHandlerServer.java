package techokami.computronics;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;

import paulscode.sound.SoundBuffer;
import paulscode.sound.SoundSystem;
import techokami.computronics.gui.GuiTapePlayer;
import techokami.computronics.tile.TileTapeDrive;
import techokami.computronics.tile.TileTapeDrive.State;
import techokami.lib.TechoLibMod;
import techokami.lib.audio.DFPWM;
import techokami.lib.audio.StreamingAudioPlayer;
import techokami.lib.network.MessageHandlerBase;
import techokami.lib.network.Packet;
import techokami.lib.util.GuiUtils;
import techokami.lib.util.WorldUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.event.sound.PlayStreamingSourceEvent;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class NetworkHandlerServer extends MessageHandlerBase {
	@Override
	public void onMessage(Packet packet, INetHandler handler, EntityPlayer player, int command)
			throws IOException {
		switch(command) {
			case Packets.PACKET_TAPE_GUI_STATE: {
				TileEntity entity = packet.readTileEntity();
				State state = State.values()[packet.readUnsignedByte()];
				if(entity instanceof TileTapeDrive) {
					TileTapeDrive tile = (TileTapeDrive)entity;
					tile.switchState(state);
				}
			} break;
		}
	}
}
