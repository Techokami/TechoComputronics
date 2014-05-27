package techokami.computronics.integration.waila;

import techokami.computronics.tile.TileTapeDrive;
import mcp.mobius.waila.api.IWailaRegistrar;

public class IntegrationWaila {
	public static void register(IWailaRegistrar reg) {
		reg.registerBodyProvider(new WailaTapeDrive(), TileTapeDrive.class);
	}
}
