package javabot;

import javabot.types.UnitType.UnitTypes;

public class Log {
	
	private static int counter = 1;
	
	public static void message(TerrestrianXVR xvr, String txt) {
		xvr.getBwapi().printText("(" + counter++ + ".) " + txt);
	}

	
	public static void messageBuild(TerrestrianXVR xvr, UnitTypes type) {
		String building = "#" + type.ordinal();
		
		if (type.ordinal() == UnitTypes.Terran_Barracks.ordinal()) {
			building = "Barracks";
		}
		else if (type.ordinal() == UnitTypes.Terran_Supply_Depot.ordinal()) {
			building = "Supply Depot";
		}
		else if (type.ordinal() == UnitTypes.Terran_Command_Center.ordinal()) {
			building = "Command Center";
		}
		
		message(xvr, "Trying to build " + building);		
	}

}
