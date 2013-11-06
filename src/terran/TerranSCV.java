package terran;

import javabot.TerrestrianXVR;
import javabot.model.Unit;
import javabot.types.UnitType.UnitTypes;

public class TerranSCV {

	public static void act(TerrestrianXVR xvr) {
//		int totalExplorers = xvr.countUnitsOfType(UnitTypes.Terran_SCV);
		
		int counter = 0;
		for (Unit scv : xvr.getUnitsOfType(UnitTypes.Terran_SCV)) {
//			if (counter != 5) {
				TerranSCV.act(xvr, scv);
//			}
//			else {
//				TerranMapExploration.explore(xvr, scv);
//			}
			
			counter++;
		}
	}
	
	public static void act(TerrestrianXVR xvr, Unit scv) {
		if (scv.isIdle()) {
			
			// Find the nearest base for this SCV
			Unit nearestBase = TerranCommandCenter.getNearestBaseForUnit(xvr, scv);
			
			// If base exists try to gather resources
			if (nearestBase != null) {
				gatherResources(xvr, scv, nearestBase);
			}
		}
	}

	private static void gatherResources(TerrestrianXVR xvr, Unit scv,
			Unit nearestBase) {
		if (true) {
			gatherMinerals(xvr, scv, nearestBase);
		}
	}

	private static void gatherMinerals(TerrestrianXVR xvr, Unit scv,
			Unit nearestBase) {
		
		// Find the closest mineral patch (if we see any)
		int closestId = -1;
		double closestDist = 99999999;
		for (Unit mineral : xvr.getMineralsUnits()) {
			double distance = xvr.getDistanceBetween(nearestBase, mineral);
			if ((closestId == -1) || (distance < closestDist)) {
				closestDist = distance;
				closestId = mineral.getID();
			}
		}
		
		// If we found it send this worker to gather it.
		if (closestId != -1) {
			xvr.getBwapi().rightClick(scv.getID(), closestId);
		}
	}


}
