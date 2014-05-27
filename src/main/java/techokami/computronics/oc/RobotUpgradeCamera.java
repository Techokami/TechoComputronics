package techokami.computronics.oc;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;
import techokami.computronics.util.Camera;
import li.cil.oc.api.Network;
import li.cil.oc.api.machine.Robot;
import li.cil.oc.api.network.Arguments;
import li.cil.oc.api.network.Callback;
import li.cil.oc.api.network.Context;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.ManagedEnvironment;

public class RobotUpgradeCamera extends ManagedEnvironment {
	private final TileEntity entity;
	private final Robot robot;
	public RobotUpgradeCamera(TileEntity entity) {
		this.entity = entity;
		this.robot = (Robot)entity;
		this.node = Network.newNode(this, Visibility.Network).withConnector().withComponent("camera", Visibility.Neighbors).create();
	}

	private final Camera camera = new Camera();
	private static final int CALL_LIMIT = 15;
	
    @Callback(direct = true, limit = CALL_LIMIT)
    public Object[] setRayDirection(Context context, Arguments args) {
    	camera.reset();
    	float x = args.count() == 2 ? (float)args.checkDouble(0) : 0.0F;
    	float y = args.count() == 2 ? (float)args.checkDouble(1) : 0.0F;
    	if(args.count() == 2) {
        	int l = MathHelper.floor_double((double)(robot.player().rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        	l = Direction.directionToFacing[l];
        	return new Object[]{
    			camera.setRayDirection(entity.getWorldObj(), entity.xCoord, entity.yCoord, entity.zCoord,
    					ForgeDirection.getOrientation(l),
    					(float)args.checkDouble(0), (float)args.checkDouble(1))
    		};
    	}
    	return null;
    }
    
    @Callback(direct = true, limit = CALL_LIMIT)
    public Object[] distance(Context context, Arguments args) {
    	setRayDirection(context, args);
    	return new Object[]{camera.getDistance()};
    }
    
    @Callback(direct = true, limit = CALL_LIMIT)
    public Object[] distanceUp(Context context, Arguments args) {
    	camera.reset();
    	if(args.count() == 2) {
    		camera.setRayDirection(entity.getWorldObj(), entity.xCoord, entity.yCoord, entity.zCoord,
    				ForgeDirection.UP,
    				(float)args.checkDouble(0), (float)args.checkDouble(1));
    	} else {
    		camera.setRayDirection(entity.getWorldObj(), entity.xCoord, entity.yCoord, entity.zCoord,
    				ForgeDirection.UP,
    				0.0F, 0.0F);
    	}
    	return new Object[]{camera.getDistance()};
    }
    
    @Callback(direct = true, limit = CALL_LIMIT)
    public Object[] distanceDown(Context context, Arguments args) {
    	camera.reset();
    	if(args.count() == 2) {
    		camera.setRayDirection(entity.getWorldObj(), entity.xCoord, entity.yCoord, entity.zCoord,
    				ForgeDirection.DOWN,
    				(float)args.checkDouble(0), (float)args.checkDouble(1));
    	} else {
    		camera.setRayDirection(entity.getWorldObj(), entity.xCoord, entity.yCoord, entity.zCoord,
    				ForgeDirection.DOWN,
    				0.0F, 0.0F);
    	}
    	return new Object[]{camera.getDistance()};
    }
    
    @Callback(direct = true, limit = CALL_LIMIT / 2)
    public Object[] block(Context context, Arguments args) {
    	setRayDirection(context, args);
    	return new Object[]{camera.getBlockData()};
    }
}
