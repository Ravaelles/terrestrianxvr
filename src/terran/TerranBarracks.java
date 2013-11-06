package terran;

import java.util.ArrayList;

import javabot.TerrestrianXVR;
import javabot.model.Unit;
import javabot.types.UnitType.UnitTypes;

public class TerranBarracks {

	private static final UnitTypes buildingType = UnitTypes.Terran_Barracks;

	public static ArrayList<Unit> getBarracks(TerrestrianXVR xvr) {
		return xvr.getUnitsOfType(buildingType);
	}

	public static void checkIfBuild(TerrestrianXVR xvr) {
		if (xvr.canAfford(150)) {
			if ((TerranConstructing.getBuildingsCount(xvr, buildingType) <= 2)
					|| (xvr.getMinerals() >= 740)) {
				TerranConstructing.construct(xvr, buildingType);
			}
		}
	}

	protected static void act(TerrestrianXVR xvr, Unit barracks) {
		if (xvr.canAfford(50, 0, 1)) {
			if (barracks.getTrainingQueueSize() == 0) {
				xvr.buildUnit(barracks, UnitTypes.Terran_Marine);
			}
		}
	}

}
