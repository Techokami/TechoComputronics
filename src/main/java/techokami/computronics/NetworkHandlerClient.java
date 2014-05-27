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

public class NetworkHandlerClient extends MessageHandlerBase {
	private static final AudioFormat DFPWM_DECODED_FORMAT = new AudioFormat(32768, 8, 1, false, false);

	@Override
	public void onMessage(Packet packet, INetHandler handler, EntityPlayer player, int command)
			throws IOException {
		switch(command) {
			case Packets.PACKET_TAPE_GUI_STATE: {
				TileEntity entity = packet.readTileEntity();
				State state = State.values()[packet.readUnsignedByte()];
				int volume = packet.readByte() & 127;
				if(entity instanceof TileTapeDrive) {
					TileTapeDrive tile = (TileTapeDrive)entity;
					tile.switchState(state);
				}
			} break;
			case Packets.PACKET_AUDIO_DATA: {
				int dimId = packet.readInt();
				int x = packet.readInt();
				int y = packet.readInt();
				int z = packet.readInt();
				int packetId = packet.readInt();
				int codecId = packet.readInt();
				short packetSize = packet.readShort();
				short volume = packet.readByte();
				byte[] data = packet.readByteArrayData(packetSize);
				byte[] audio = new byte[packetSize * 8];
				String sourceName = "dfpwm_"+codecId;
				StreamingAudioPlayer codec = Computronics.instance.audio.getPlayer(codecId);
	
				if(dimId != WorldUtils.getCurrentClientDimension()) return;
				
				codec.decompress(audio, data, 0, 0, packetSize);
				for(int i = 0; i < (packetSize * 8); i++) {
					// Convert signed to unsigned data
					audio[i] = (byte)(((int)audio[i] & 0xFF) ^ 0x80);
				}
				
				if((codec.lastPacketId + 1) != packetId) {
					codec.reset();
				}
				codec.setSampleRate(packetSize * 32);
				codec.setDistance((float)Computronics.TAPEDRIVE_DISTANCE);
				codec.setVolume(volume/127.0F);
				codec.playPacket(audio, x, y, z);
				codec.lastPacketId = packetId;
			} break;
			case Packets.PACKET_AUDIO_STOP: {
				int codecId = packet.readInt();
				Computronics.instance.audio.removePlayer(codecId);
			} break;
		}
	}
}
