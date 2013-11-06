package terran;

import java.awt.Point;
import java.util.TreeSet;

import rafaelles.RUtilities;
import javabot.JNIBWAPI;
import javabot.Log;
import javabot.TerrestrianXVR;
import javabot.model.Unit;
import javabot.types.UnitType.UnitTypes;

public class TerranConstructing {

	public static void act(TerrestrianXVR xvr) {
		 TerranBarracks.checkIfBuild(xvr);
		TerranSupplyDepot.checkIfBuild(xvr);
	}

	public static Point findBuildTile(TerrestrianXVR xvr, int builderID,
			UnitTypes type, Unit building) {
		return findBuildTile(xvr, builderID, type.ordinal(), building.getX(),
				building.getY());
	}

	public static Point findBuildTile(TerrestrianXVR xvr, int builderID,
			int buildingTypeID, int x, int y) {
		JNIBWAPI bwapi = xvr.getBwapi();

		Point pointToBuild = new Point(-1, -1);
		int stopDist = 100;
		int maxDist = 4;

		int tileX = x / 32;
		int tileY = y / 32;

		// Refinery, Assimilator, Extractor
		if (bwapi.getUnitType(buildingTypeID).isRefinery()) {
			for (Unit n : bwapi.getNeutralUnits()) {
				if ((n.getTypeID() == UnitTypes.Resource_Vespene_Geyser
						.ordinal())
						&& (Math.abs(n.getTileX() - tileX) < stopDist)
						&& (Math.abs(n.getTileY() - tileY) < stopDist)) {
					return new Point(n.getTileX(), n.getTileY());
				}
			}
		}

		// while ((maxDist < stopDist) && (pointToBuild.x == -1)) {
		//
		// for (int attempt = 0; attempt < 5; attempt++) {
		// int i = tileX + maxDist - RUtilities.rand(0, 2 * maxDist);
		// int j = tileY + maxDist - RUtilities.rand(0, 2 * maxDist);
		// if (bwapi.canBuildHere(builderID, i, j, buildingTypeID, false)) {
		// // units that are blocking the tile
		// boolean unitsInWay = false;
		// for (Unit u : bwapi.getAllUnits()) {
		// if (u.getID() == builderID) {
		// continue;
		// }
		// if ((Math.abs(u.getTileX() - i) < 4)
		// && (Math.abs(u.getTileY() - j) < 4)) {
		// unitsInWay = true;
		// }
		// }
		// if (!unitsInWay) {
		// pointToBuild.x = i;
		// pointToBuild.y = j;
		// return pointToBuild;
		// }
		// }
		// maxDist++;
		// }
		// }

		while ((maxDist < stopDist) && (pointToBuild.x == -1)) {
			for (int i = tileX - maxDist; i <= tileX + maxDist; i++) {
				for (int j = tileY - maxDist; j <= tileY + maxDist; j++) {

					// System.out.println("i: " + i + ", j: " + j + " = "
					// + bwapi.canBuildHere(builderID, i, j, buildingTypeID,
					// false));
					if (bwapi.canBuildHere(builderID, i, j, buildingTypeID,
							true)) {

						// units that are blocking the tile
						boolean unitsInWay = false;
						for (Unit u : bwapi.getAllUnits()) {
							if (u.getID() == builderID) {
								continue;
							}
							if ((Math.abs(u.getTileX() - i) < 4)
									&& (Math.abs(u.getTileY() - j) < 4)) {
								unitsInWay = true;
							}
						}
						if (!unitsInWay) {
							pointToBuild.x = i;
							pointToBuild.y = j;
							return pointToBuild;
						}
					}
				}
			}
			maxDist += 2;
		}

		if (pointToBuild.x == -1) {
			bwapi.printText("Unable to find suitable build position for "
					+ bwapi.getUnitType(buildingTypeID).getName());
		}
		return pointToBuild;
	}

	public static void construct(TerrestrianXVR xvr, UnitTypes building) {
		// if (xvr.getMinerals() < minerals || xvr.getGas() < gas
		// || xvr.getFirstBase() == null) {
		// return;
		// }

		// try to find the worker near our home position
		Unit workerUnit = TerrestrianXVR.getUnitOfTypeNearestTo(xvr,
				UnitTypes.Terran_SCV, xvr.getFirstBase());
		// int worker =
		// xvr.getBot().getNearestUnit(UnitTypes.Terran_SCV.ordinal(),
		// xvr.getBot().homePositionX, xvr.getBot().homePositionY);
		if (workerUnit != null) {
			int workerId = workerUnit.getID();

			// if we found him, try to select appropriate build tile
			// position for the building
			Point buildTile = TerranConstructing.findBuildTile(xvr, workerId,
					building, xvr.getFirstBase());
			// Point buildTile = xvr.getBuildTile(worker,
			// building.ordinal(), xvr.getBot().homePositionX,
			// xvr.getBot().homePositionY);

			// if we found a good build position, and we aren't already
			// constructing this building order our worker to build it
			// && (!xvr.weAreBuilding(building))
			if (buildTile.x != -1) {
				Log.messageBuild(xvr, building);
				xvr.build(workerId, buildTile, building);

				// xvr.getBwapi().drawCircle(buildTile.x * 30, buildTile.y * 30,
				// 15, 0x11, false, true);
				// xvr.getBwapi().drawCircle(buildTile.x, buildTile.y, 15, 0x11,
				// false, true);
			}
		}

		// try to find the worker near our home position
		// int worker =
		// xvr.getBot().getNearestUnit(UnitTypes.Terran_SCV.ordinal(),
		// xvr.getBot().homePositionX, xvr.getBot().homePositionY);
		// Unit workerUnit = TerrestrianXVR.getUnitOfTypeNearestTo(xvr,
		// UnitTypes.Terran_SCV, xvr.getFirstBase());
		// int worker = workerUnit.getID();
		//
		// if (worker != -1) {
		// // if we found him, try to select appropriate build tile position for
		// supply depot (near our home base)
		// Point buildTile = xvr.getBuildTile(worker,
		// UnitTypes.Terran_Supply_Depot.ordinal(), xvr.getBot().homePositionX,
		// xvr.getBot().homePositionY);
		// // if we found a good build position, and we aren't already
		// constructing a Supply Depot,
		// // order our worker to build it
		// if ((buildTile.x != -1) &&
		// (!xvr.weAreBuilding(UnitTypes.Terran_Supply_Depot))) {
		// xvr.getBwapi().build(worker, buildTile.x, buildTile.y,
		// UnitTypes.Terran_Supply_Depot.ordinal());
		// }
		// }
	}

	// ===========================================================
	// UTILITIES

	public static int getBuildingsCount(TerrestrianXVR xvr,
			UnitTypes buildintType) {
		return xvr.getUnitsOfType(buildintType).size();
	}

}
