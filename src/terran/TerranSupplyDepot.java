package terran;

import javabot.TerrestrianXVR;
import javabot.types.UnitType.UnitTypes;

public class TerranSupplyDepot {

	private static final UnitTypes buildingType = UnitTypes.Terran_Supply_Depot;

	public static void checkIfBuild(TerrestrianXVR xvr) {
		if (xvr.canAfford(100)) {
			
			// It only makes sense to build Supply Depot if supplies less than X.
			if (xvr.getSuppliesFree() <= 4) {
				TerranConstructing.construct(xvr, buildingType);
			}
		}
	}

}
