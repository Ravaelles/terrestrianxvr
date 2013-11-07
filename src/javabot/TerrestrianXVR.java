package javabot;

import java.awt.Point;
import java.util.ArrayList;

import javabot.model.Unit;
import javabot.types.UnitType.UnitTypes;
import terran.TerranCommandCenter;
import terran.TerranConstructing;
import terran.TerranSCV;
import terran.TerranUnits;
import terran.TerranUnitsCreation;

public class TerrestrianXVR {
	
	/** Less = faster. */
	public static final int GAME_SPEED = 7;

	private JavaBot bot;
	private JNIBWAPI bwapi;

	public TerrestrianXVR(JavaBot bot) {
		this.bot = bot;
		this.bwapi = bot.getBwapi();
	}

	// =====================================================

	public void act() {
		// This method is called every 30th frame (approx. once a
		// second). You can use other methods in this class, but the
		// majority of your agent's behaviour will probably be here.

		// First, let's train workers at our Command Center.
		handleCommandCenters();

		// Now let's mine minerals with your idle workers.
		handleSCVsBehaviour();
		
		// And let's build some Supply Depots if we are low on supply (if free
		// supply is less than 3).
		handleConstruction();
		
		// Build some army units if possible
		handleUnitsCreation();
		
		// Handle behavior of army units
		handleArmyBehavior();
	}

	public void handleCommandCenters() {
		for (Unit base : getUnitsOfType(UnitTypes.Terran_Command_Center
				.ordinal())) {
			TerranCommandCenter.act(this, base);
		}
	}

	public void handleSCVsBehaviour() {
		TerranSCV.act(this);
	}

	public void handleConstruction() {
		TerranConstructing.act(this);
	}

	public void handleUnitsCreation() {
		TerranUnitsCreation.act(this);
	}
	
	public void handleArmyBehavior() {
		TerranUnits.act(this);
	}
	
	// =========================================================
	// Getters

	public JavaBot getBot() {
		return bot;
	}

	public JNIBWAPI getBwapi() {
		return bwapi;
	}

	// =========================================================
	// UTILITIES

	public void buildUnit(Unit building, UnitTypes type) {
		getBwapi().train(building.getID(), type.ordinal());
	}
	
	public ArrayList<Unit> getUnitsOfType(UnitTypes unitType) {
		return getUnitsOfType(unitType.ordinal());
	}
	
	public ArrayList<Unit> getUnitsOfType(int unitType) {
		ArrayList<Unit> objectsOfThisType = new ArrayList<Unit>();

		for (Unit unit : bwapi.getMyUnits()) {
			if (unit.getTypeID() == unitType) {
				objectsOfThisType.add(unit);
			}
		}

		return objectsOfThisType;
	}

	public int countUnitsOfType(UnitTypes unitType) {
		int counter = 0;

		for (Unit unit : bwapi.getMyUnits()) {
			if (unit.getTypeID() == unitType.ordinal()) {
				counter++;
			}
		}

		return counter;
	}
	
	public ArrayList<Unit> getUnitsNonSCV() {
		ArrayList<Unit> objectsOfThisType = new ArrayList<Unit>();

		for (Unit unit : bwapi.getMyUnits()) {
			if (!unit.isCompleted() && !unit.isConstructing() 
					&& unit.getTypeID() != UnitTypes.Terran_SCV.ordinal()) {
				objectsOfThisType.add(unit);
			}
		}

		return objectsOfThisType;
	}

	public int getMinerals() {
		return bwapi.getSelf().getMinerals();
	}

	public int getGas() {
		return bwapi.getSelf().getGas();
	}

	public int getSuppliesFree() {
		return (bwapi.getSelf().getSupplyTotal() - bwapi.getSelf().getSupplyUsed()) / 2;
	}

	public int getSuppliesTotal() {
		return (bwapi.getSelf().getSupplyTotal()) / 2;
	}

	public double getDistanceBetween(Unit u1, Unit u2) {
		return Math.sqrt((u1.getX() - u2.getX()) * (u1.getX() - u2.getX())
				+ (u1.getY() - u2.getY()) * (u1.getY() - u2.getY()));
	}

	public ArrayList<Unit> getMineralsUnits() {
		ArrayList<Unit> objectsOfThisType = new ArrayList<Unit>();

		for (Unit unit : bwapi.getNeutralUnits()) {
			if (unit.getTypeID() == UnitTypes.Resource_Mineral_Field.ordinal()) {
				objectsOfThisType.add(unit);
			}
		}

		return objectsOfThisType;
	}

	public ArrayList<Unit> getGeysersUnits() {
		ArrayList<Unit> objectsOfThisType = new ArrayList<Unit>();

		for (Unit unit : bwapi.getNeutralUnits()) {
			if (unit.getTypeID() == UnitTypes.Resource_Vespene_Geyser.ordinal()) {
				objectsOfThisType.add(unit);
			}
		}

		return objectsOfThisType;
	}

	public Unit getFirstBase() {
		ArrayList<Unit> bases = getUnitsOfType(UnitTypes.Terran_Command_Center
				.ordinal());
		if (!bases.isEmpty()) {
			return bases.get(0);
		} else {
			return null;
		}
	}

	public static Unit getUnitOfTypeNearestTo(TerrestrianXVR xvr, UnitTypes type,
			Unit closeTo) {
		double nearestDistance = 999999;
		Unit nearestUnit = null;

		for (Unit otherUnit : xvr.getUnitsOfType(type)) {
			if (!otherUnit.isCompleted()) {
				continue;
			}
			
			double distance = xvr.getDistanceBetween(otherUnit, closeTo);
			if (distance < nearestDistance) {
				nearestDistance = distance;
				nearestUnit = otherUnit;
			}
		}

		return nearestUnit;
	}

	public boolean weAreBuilding(UnitTypes type) {
		for (Unit unit : bwapi.getMyUnits()) {
			if ((unit.getTypeID() == type.ordinal()) && (!unit.isCompleted())) {
				return true;
			}
			if (bwapi.getUnitType(unit.getTypeID()).isWorker()
					&& unit.getConstructingTypeID() == type.ordinal()) {
				return true;
			}
		}
		return false;
	}
	
	public void build(int worker, Point buildTile, UnitTypes building) {
		getBwapi().build(worker, buildTile.x, buildTile.y, building.ordinal());
	}

	public boolean canAfford(int minerals) {
		return canAfford(minerals, 0, 0);
	}
	
	public boolean canAfford(int minerals, int gas) {
		return canAfford(minerals, gas, 0);
	}
	
	public boolean canAfford(int minerals, int gas, int supply) {
		if (minerals > 0 && getMinerals() < minerals) {
			return false;
		}
		if (gas > 0 && getGas() < gas) {
			return false;
		}
		if (supply > 0 && getSuppliesFree() < supply) {
			return false;
		}
		return true;
	}

}
