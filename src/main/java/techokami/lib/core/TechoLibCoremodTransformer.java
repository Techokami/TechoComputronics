package techokami.lib.core;

import java.io.IOException;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;

public class TechoLibCoremodTransformer extends AccessTransformer {

	public TechoLibCoremodTransformer() throws IOException {
		super("asielib_at.cfg");
	}

}
