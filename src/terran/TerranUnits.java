package terran;

import javabot.TerrestrianXVR;
import javabot.model.Unit;

public class TerranUnits {

	public static void act(TerrestrianXVR xvr) {
		
	}

	
	public static void moveTo(TerrestrianXVR xvr, Unit unit, int x, int y) {
		xvr.getBwapi().move(unit.getID(), x, y);
	}

	public static void attackTo(TerrestrianXVR xvr, Unit unit, int x, int y) {
		xvr.getBwapi().attack(unit.getID(), x, y);
	}

}
