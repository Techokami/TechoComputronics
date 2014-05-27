package techokami.lib;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jcraft.jogg.Packet;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.MinecraftForge;
import techokami.lib.api.TechoLibAPI;
import techokami.lib.api.chat.INicknameHandler;
import techokami.lib.api.chat.INicknameRepository;
import techokami.lib.chat.ChatHandler;
import techokami.lib.chat.NicknameNetworkHandler;
import techokami.lib.chat.NicknameRepository;
import techokami.lib.client.BlockBaseRender;
import techokami.lib.integration.Integration;
import techokami.lib.network.PacketHandler;
import techokami.lib.shinonome.EventKeyClient;
import techokami.lib.shinonome.EventKeyServer;
import techokami.lib.shinonome.ItemKey;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid="Techolib", name="TechoLib", version="0.2.3")
public class TechoLibMod extends TechoLibAPI {
	public Configuration config;
	public static Integration integration;
	public static Random rand = new Random();
	public static Logger log;
	public static ChatHandler chat;
	public static NicknameRepository nick;
	public static ItemKey itemKey;
	public static PacketHandler packet;
	public static EventKeyClient keyClient = new EventKeyClient();
	public static EventKeyServer keyServer = new EventKeyServer();
	
	@Instance(value="Techolib")
	public static TechoLibMod instance;
	
	@SidedProxy(clientSide="techokami.lib.ClientProxy", serverSide="techokami.lib.CommonProxy")	
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		TechoLibAPI.instance = this;
		integration = new Integration();
		log = LogManager.getLogger("Techolib");
		
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		
		chat = new ChatHandler(config);

    	MinecraftForge.EVENT_BUS.register(chat);
    	MinecraftForge.EVENT_BUS.register(new TechoLibEvents());
    	
		if(System.getProperty("user.dir").indexOf(".Techolauncher") >= 0) {
			log.info("Hey, you! Yes, you! Thanks for using TechoLauncher! ~Techo");
		}
		
		itemKey = new ItemKey();
		GameRegistry.registerItem(itemKey, "item.Techotweaks.key");
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		if(proxy.isClient()) {
			MinecraftForge.EVENT_BUS.register(keyClient);
			FMLCommonHandler.instance().bus().register(keyClient);
			
			new BlockBaseRender();
		}
		MinecraftForge.EVENT_BUS.register(keyServer);

		packet = new PacketHandler("Techolib", new NetworkHandlerClient(), null);
		
		nick = new NicknameRepository();
		nick.loadNicknames();
    	MinecraftForge.EVENT_BUS.register(nick);
		
		FMLCommonHandler.instance().bus().register(new NicknameNetworkHandler());
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		//TickRegistry.registerTickHandler(keyClient, Side.CLIENT);
	}
	
	@EventHandler
	public void onServerStart(FMLServerStartingEvent event) {
    	chat.registerCommands(event);
	}
	
	@EventHandler
	public void onServerStop(FMLServerStoppingEvent event) {
		nick.saveNicknames();
	}
	
	public void registerNicknameHandler(INicknameHandler handler) {
		if(nick != null)
			nick.addHandler(handler);
	}
	
	public INicknameRepository getNicknameRepository() { return nick; }
}
