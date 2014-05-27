package techokami.computronics.storage;

import java.io.File;
import java.util.HashMap;
import java.util.Random;

import net.minecraftforge.common.DimensionManager;
import techokami.computronics.Computronics;
import techokami.lib.util.MiscUtils;

public class StorageManager {
	// Map
	private static Random rand = new Random();
	
	// Class
	private File saveDir;
	
	public StorageManager() {
		this.saveDir = new File(DimensionManager.getCurrentSaveRootDirectory(), "computronics");
		if(!this.saveDir.exists() && !this.saveDir.mkdir()) {
			Computronics.log.error("COULD NOT CREATE SAVE DIRECTORY: " + this.saveDir.getAbsolutePath());
		}
	}
	
	private String filename(String storageName) {
		return storageName + ".dsk";
	}
	
	public Storage newStorage(int size) {
		String storageName;
		while(true) {
			byte[] nameHex = new byte[16];
			rand.nextBytes(nameHex);
			storageName = MiscUtils.asHexString(nameHex);
			if(!exists(storageName)) break;
		}
		return get(storageName, size, 0);
	}
	
	public boolean exists(String name) {
		return new File(saveDir, filename(name)).exists();
	}
	
	public Storage get(String name, int size, int position) {
		return new Storage(name, new File(saveDir, filename(name)), size, position);
	}
}
