package terran;

import javabot.TerrestrianXVR;
import javabot.model.Unit;
import javabot.types.UnitType.UnitTypes;

public class TerranCommandCenter {

	public static void act(TerrestrianXVR xvr, Unit base) {
		// if it's training queue is empty
		if (base.getTrainingQueueSize() == 0) {
			// check if we have enough minerals and supply, and (if we do)
			// train one worker (Terran_SCV)
			if ((xvr.getMinerals() >= 50) && (xvr.getSuppliesFree() >= 1)) {
				xvr.buildUnit(base, UnitTypes.Terran_SCV);
			}
		}
	}
	
	public static Unit getNearestBaseForUnit(TerrestrianXVR xvr, Unit unit) {
		double nearestDistance = 9999999;
		Unit nearestBase = null;
		
		for (Unit base : xvr.getUnitsOfType(UnitTypes.Terran_Command_Center.ordinal())) {
			double distance = xvr.getDistanceBetween(base, unit);
			if (distance < nearestDistance) {
				distance = nearestDistance;
				nearestBase = base;
			}
		}
		
		return nearestBase;
	}

}

//// if this unit is a command center (Terran_Command_Center)
//if (unit.getTypeID() == UnitTypes.Terran_Command_Center.ordinal()) {
//	// if it's training queue is empty
//	if (unit.getTrainingQueueSize() == 0) {
//		// check if we have enough minerals and supply, and (if we do)
//		// train one worker (Terran_SCV)
//		if ((bwapi.getSelf().getMinerals() >= 50)
//				&& (bwapi.getSelf().getSupplyTotal()
//						- bwapi.getSelf().getSupplyUsed() >= 2))
//			bwapi.train(unit.getID(), UnitTypes.Terran_SCV.ordinal());
//	}
//}