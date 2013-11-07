package javabot;

import java.awt.Point;

import javabot.model.Unit;
import javabot.types.UnitType.UnitTypes;
import javabot.util.BWColor;

public class JavaBot implements BWAPIEventListener {

	// Some miscelaneous variables. Feel free to add yours.
	public int homePositionX;
	public int homePositionY;
	private JNIBWAPI bwapi;
	private TerrestrianXVR xvr;

	public static void main(String[] args) {
		new JavaBot();
	}

	public JavaBot() {
		bwapi = new JNIBWAPI(this);
		xvr = new TerrestrianXVR(this);
		bwapi.start();
	}

	public JNIBWAPI getBwapi() {
		return bwapi;
	}

	public TerrestrianXVR getXvr() {
		return xvr;
	}

	public void connected() {
		bwapi.loadTypeData();
	}

	// Method called at the beginning of the game.
	public void gameStarted() {
		System.out.println("Game Started");

		// allow me to manually control units during the game
		bwapi.enableUserInput();

		// set game speed to 30 (0 is the fastest. Tournament speed is 20)
		// You can also change the game speed from within the game by "/speed X"
		// command.
		bwapi.setGameSpeed(TerrestrianXVR.GAME_SPEED);

		// analyze the map
		bwapi.loadMapData(true);

		// ============== YOUR CODE GOES HERE =======================

		// This is called at the beginning of the game. You can
		// initialize some data structures (or do something similar)
		// if needed. For example, you should maintain a memory of seen
		// enemy buildings.

		// bwapi.printText("This map is called " + bwapi.getMap().getName());
		// bwapi.printText("My race ID: "
		// + String.valueOf(bwapi.getSelf().getRaceID())); // Z=0,T=1,P=2
		// bwapi.printText("Enemy race ID: "
		// + String.valueOf(bwapi.getEnemies().get(0).getRaceID())); //
		// Z=0,T=1,P=2

		// ==========================================================
	}

	// Method called once every second.
	public void act() {

		// ============== YOUR CODE GOES HERE =======================
		xvr.act();
	}

	// ==========================================================

	// Method called on every frame (approximately 30x every second).
	public void gameUpdate() {

		// Remember our homeTilePosition at the first frame
		if (bwapi.getFrameCount() == 1) {
			int cc = getNearestUnit(UnitTypes.Terran_Command_Center.ordinal(),
					0, 0);
			if (cc == -1) {
				cc = getNearestUnit(UnitTypes.Zerg_Hatchery.ordinal(), 0, 0);
			}
			if (cc == -1) {
				cc = getNearestUnit(UnitTypes.Protoss_Nexus.ordinal(), 0, 0);
			}
			homePositionX = bwapi.getUnit(cc).getX();
			homePositionY = bwapi.getUnit(cc).getY();

		}

		// Draw debug information on screen
		drawDebugInfo();

		// Call the act() method every 30 frames
		if (bwapi.getFrameCount() % 30 == 0) {
			act();
		}

	}

	// Some additional event-related methods.
	public void gameEnded() {
	}

	public void matchEnded(boolean winner) {
	}

	public void nukeDetect(int x, int y) {
	}

	public void nukeDetect() {
	}

	public void playerLeft(int id) {
	}

	public void unitCreate(int unitID) {
	}

	public void unitDestroy(int unitID) {
	}

	public void unitDiscover(int unitID) {
	}

	public void unitEvade(int unitID) {
	}

	public void unitHide(int unitID) {
	}

	public void unitMorph(int unitID) {
	}

	public void unitShow(int unitID) {
	}

	public void keyPressed(int keyCode) {
	}

	// Returns the id of a unit of a given type, that is closest to a pixel
	// position (x,y), or -1 if we
	// don't have a unit of this type
	public int getNearestUnit(int unitTypeID, int x, int y) {
		int nearestID = -1;
		double nearestDist = 9999999;
		for (Unit unit : bwapi.getMyUnits()) {
			if ((unit.getTypeID() != unitTypeID) || (!unit.isCompleted())) {
				continue;
			}
			double dist = Math.sqrt(Math.pow(unit.getX() - x, 2)
					+ Math.pow(unit.getY() - y, 2));
			if (nearestID == -1 || dist < nearestDist) {
				nearestID = unit.getID();
				nearestDist = dist;
			}
		}
		return nearestID;
	}

	//
	// // Returns the Point object representing the suitable build tile position
	// // for a given building type near specified pixel position (or
	// Point(-1,-1)
	// // if not found)
	// // (builderID should be our worker)
	// public Point getBuildTile(int builderID, int buildingTypeID, int x, int
	// y) {
	// Point pointToBuild = new Point(-1, -1);
	// int maxDist = 3;
	// int stopDist = 40;
	// int tileX = x / 32;
	// int tileY = y / 32;
	//
	// // Refinery, Assimilator, Extractor
	// if (bwapi.getUnitType(buildingTypeID).isRefinery()) {
	// for (Unit n : bwapi.getNeutralUnits()) {
	// if ((n.getTypeID() == UnitTypes.Resource_Vespene_Geyser
	// .ordinal())
	// && (Math.abs(n.getTileX() - tileX) < stopDist)
	// && (Math.abs(n.getTileY() - tileY) < stopDist)) {
	// return new Point(n.getTileX(), n.getTileY());
	// }
	// }
	// }
	//
	// while ((maxDist < stopDist) && (pointToBuild.x == -1)) {
	// for (int i = tileX - maxDist; i <= tileX + maxDist; i++) {
	// for (int j = tileY - maxDist; j <= tileY + maxDist; j++) {
	// if (bwapi.canBuildHere(builderID, i, j, buildingTypeID,
	// false)) {
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
	//
	// // // psi power for Protoss (this seems to work out of
	// // the box)
	// // if
	// // (bwapi.getUnitType(buildingTypeID).isRequiresPsi()) {
	// // }
	// }
	// }
	// }
	// maxDist += 2;
	// }
	//
	// if (pointToBuild.x == -1) {
	// bwapi.printText("Unable to find suitable build position for "
	// + bwapi.getUnitType(buildingTypeID).getName());
	// }
	// return pointToBuild;
	// }

	// Draws debug information on the screen.
	// Reimplement this function however you want.
	public void drawDebugInfo() {
		Log.drawDebug(xvr);
	}
}
