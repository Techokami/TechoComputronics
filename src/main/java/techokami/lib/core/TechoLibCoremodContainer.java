package techokami.lib.core;

import java.util.Arrays;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.ModMetadata;

public class TechoLibCoremodContainer extends DummyModContainer {
	public TechoLibCoremodContainer() {
		super(new ModMetadata());
		ModMetadata meta = super.getMetadata();
		meta.modId = "asielibcore";
		meta.name = "TechoLib CoreMod";
		meta.description = "Patches things in vanilla Minecraft for TechoLib and dependent mods";
		meta.authorList = Arrays.asList(new String[]{"asiekierka"});
		meta.parent = "asielib";
		this.setEnabledState(true);
	}
	
	public Disableable canBeDisabled() {
		return Disableable.NEVER;
	}
}
