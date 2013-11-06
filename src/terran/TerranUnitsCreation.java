package terran;

import java.util.ArrayList;

import javabot.TerrestrianXVR;
import javabot.model.Unit;

public class TerranUnitsCreation {

	public static void act(TerrestrianXVR xvr) {
		ArrayList<Unit> barracksList = TerranBarracks.getBarracks(xvr);
		if (!barracksList.isEmpty()) {
			for (Unit barracks : barracksList) {
				TerranBarracks.act(xvr, barracks);
			}
		}
	}

	

}
