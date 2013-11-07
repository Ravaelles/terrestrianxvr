package javabot;

import java.awt.Point;

import javabot.model.Unit;
import javabot.types.UnitType.UnitTypes;
import javabot.util.BWColor;

public class Log {

	private static int messageCounter = 1;

	public static void drawDebug(TerrestrianXVR xvr) {
		// Draw our home position.
		xvr.getBwapi().drawText(new Point(5, 0),
				"Our home position: " + String.valueOf(xvr.getBot().homePositionX) + ","
						+ String.valueOf(xvr.getBot().homePositionY), true);

		// Draw circles over workers (blue if they're gathering minerals, green
		// if gas, yellow if they're constructing).
		for (Unit u : xvr.getBwapi().getMyUnits()) {
			if (u.isGatheringMinerals()) {
				xvr.getBwapi().drawCircle(u.getX(), u.getY(), 12, BWColor.BLUE, false,
						false);
			} else if (u.isGatheringGas()) {
				xvr.getBwapi().drawCircle(u.getX(), u.getY(), 12, BWColor.GREEN, false,
						false);
			}
		}
	}

	public static void message(TerrestrianXVR xvr, String txt) {
		xvr.getBwapi().printText("(" + messageCounter++ + ".) " + txt);
	}

	public static void messageBuild(TerrestrianXVR xvr, UnitTypes type) {
		String building = "#" + type.ordinal();

		if (type.ordinal() == UnitTypes.Terran_Barracks.ordinal()) {
			building = "Barracks";
		} else if (type.ordinal() == UnitTypes.Terran_Supply_Depot.ordinal()) {
			building = "Supply Depot";
		} else if (type.ordinal() == UnitTypes.Terran_Command_Center.ordinal()) {
			building = "Command Center";
		}

		message(xvr, "Trying to build " + building);
	}

}
