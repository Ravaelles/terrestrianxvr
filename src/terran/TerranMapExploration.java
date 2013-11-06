package terran;

import java.util.ArrayList;
import java.util.TreeSet;

import javabot.TerrestrianXVR;
import javabot.model.BaseLocation;
import javabot.model.Unit;
import rafaelles.RUtilities;

public class TerranMapExploration {
	
	private static TreeSet<BaseLocation> baseLocationsDiscovered = new TreeSet<BaseLocation>();
	
	public static void explore(TerrestrianXVR xvr, Unit explorer) {
		
		// If explorer is on its way, don't interrupt.
		if (!explorer.isIdle() && !explorer.isGatheringMinerals() && !explorer.isGatheringGas()) {
			return;
		}
		
		// Filter out visited bases.
		ArrayList<BaseLocation> possibleBases = new ArrayList<BaseLocation>();
		possibleBases.addAll(xvr.getBwapi().getMap().getBaseLocations());
		possibleBases.removeAll(baseLocationsDiscovered);
		
		// If there is any unvisited base- go there. If no- go to the random base. 
		BaseLocation randomBase;
		if (possibleBases.isEmpty()) {
			randomBase = (BaseLocation) RUtilities.getRandomListElement
					(xvr.getBwapi().getMap().getBaseLocations());
		}
		else {
			randomBase = (BaseLocation) RUtilities.getRandomListElement(possibleBases);
			
			// Add info that we've visited this place.
			baseLocationsDiscovered.add(randomBase);
		}
		
		// Send unit to the base.
		TerranUnits.attackTo(xvr, explorer, randomBase.getX(), randomBase.getY());
		
//		for (int i = 0; i < xvr.getBwapi().getMap().getBaseLocations().size(); i++) {
//			
//		}
//		xvr.getBwapi().getMap().getChokePoints().get(0).
//		xvr.getNearest
	}
		
//		for (BaseLocation b : bwapi.getMap().getBaseLocations()) {
//			// If this is a possible start location,
//			if (b.isStartLocation()) {
//				// do something. For example send some unit to a position 
//				// b.getX(),b.getY() to see, if the enemy is there.  
//			}
//		}
//	}

}
