/**
 * Name: Emnet Mekonnen
 * Date:4/01/2024
 * CSC 202
 * Project 2--Wizard class
 *  
 * Description: This class models a Wizard character in a basic
 *              role playing game. Wizards have no armor and
 *              restricted weapons, but can regain spell points. 
 *              Additionally, wizards can use spell points to cast
 *              spells which can either heal or do damage.
 * 
 * Citations of Assistance (who and what OR declare no assistance):
 * https://stackoverflow.com/questions/541749/how-to-determine-an-objects-class
 * I used this to figure out what the class of the object that is passed in the check spell method is
 * 
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Wizard extends RPGCharacter{
	/**
	 * The initial and maximum number of health points for a Wizard
	 */
	public static final int MAX_HEALTH_POINTS = 30;
	
	/**
	 * The initial and maximum number of spell points for a Wizard
	 */
	public static final int MAX_SPELL_POINTS = 20;
	
	/**
	 * The number of sides on the die rolled to try to regain 
	 */
	public static final int NUMBER_SIDES_TO_REGAIN__SPELL_POINTS = 10;
	
	/**
	 * The die with NUMBER_SIDES_TO_REGAIN_SPELL_POINTS sides to be rolls when a fight
	 * ensues or a damaging spell is cast
	 */
	public static MultiSidedDie dieToRegainPoints = new MultiSidedDie(NUMBER_SIDES_TO_REGAIN__SPELL_POINTS);
	
	private int spellPoints;         //spell points that a wizard has to cast spells
	
	/**
	 * Constructs a wizard by using the name and the max health points. Also initializes the max spell points.
	 * @param name - name of the character
	 */
	public Wizard(String name) {
		super(name, MAX_HEALTH_POINTS);
		spellPoints = MAX_SPELL_POINTS;
	}
	
	@Override
	public List<Action> getActionList(){
		List<Action> actionList = super.getActionList();
		actionList.add(0,Action.REGAIN_SPELL_POINTS);
		actionList.add(0, Action.CAST_SPELL);
		
		return actionList;
	}
	
	@Override
	public List<Weapon> getAllowedWeaponList(){
		List<Weapon> wizardWeapons = new ArrayList<>();
		wizardWeapons.add(Weapon.DAGGER);
		wizardWeapons.add(Weapon.STAFF);
		
		return wizardWeapons;
	}
	@Override
	public List<Armor> getAllowedArmorList(){
		List<Armor> wizardArmor = new ArrayList<>();
		wizardArmor.add(Armor.NO_ARMOR);
		
		return wizardArmor;
	}
	/**
	 * Uses a random value generated by a die to determine if the wizard can cast the spell and 
	 * what effect a spell has on the receiving character. 
	 * @param spell - the spell that the wizard casts
	 * @param other - the character that receives the effects of the spell
	 */
	public void castSpell(Spell spell, RPGCharacter other) {
		RPGCharacter.attackDie.roll();
		int dieRolled = attackDie.getValue();
		String spellName = spell.getName();
		int maxHealth = 0;
		if(other instanceof Fighter) {
			maxHealth = Fighter.MAX_HEALTH_POINTS;
		}else {
			maxHealth = MAX_HEALTH_POINTS;
		}
			
		if(spellPoints < spell.getCost()) {
			System.out.printf("%s doesn't have enough spell points to cast this spell.\n", getName());
		}else if(spellName.equals(Spell.HEAL.getName())) {
			System.out.printf("%s casts a heal spell at %s.\n", super.getName(), other.getName());
			if(other.getHealthPoints() - Spell.HEAL.getEffect() > maxHealth) {
				other.setHealthPoints(maxHealth);
				spellPoints -= spell.getCost();
				System.out.printf("%s's health points cannot exceed %d.\n", other.getName(), maxHealth);
				System.out.printf("%s now has %d health points.\n", other.getName(), maxHealth);
			}else {
				spellPoints -= spell.getCost();
				other.setHealthPoints(other.getHealthPoints() - Spell.HEAL.getEffect());
				System.out.printf("%s now has %d health points.\n", other.getName(), other.getHealthPoints());
			}
		}else {
			System.out.printf("%s casts %s spell at %s.\n", super.getName(), spell.getName(), other.getName());
			System.out.printf("Die rolled: %d\n", dieRolled);
			if(dieRolled > other.getArmor().getArmorClass()) {
				spellPoints -= spell.getCost();
				other.setHealthPoints(other.getHealthPoints()- spell.getEffect());
				System.out.printf("%s does %d damage to %s.\n", getName(), spell.getEffect(), other.getName());
				System.out.printf("%s now has %d health points.\n", other.getName(), other.getHealthPoints());
			}else {
				spellPoints -= spell.getCost();
				System.out.printf("%s's armor defended him. No loss of health points.\n", other.getName());
			}
				
		}
	}
		
	/**
	 * Determines how many spell points a wizard can regain by rolling a die
	 */
	public void regainSpellPoints() {
		dieToRegainPoints.roll();
		int dieRolled = dieToRegainPoints.getValue();
		System.out.printf("Die rolled: %d\n", dieRolled);
		
		if(dieRolled + spellPoints > MAX_SPELL_POINTS) {
			spellPoints = MAX_SPELL_POINTS;
			System.out.printf("%s's spell points cannot exceed %d.\n", super.getName(), MAX_SPELL_POINTS);
			System.out.printf("%s now has %d spell points.\n", super.getName(), MAX_SPELL_POINTS);
		}else {
			spellPoints += dieRolled;
			System.out.printf("%s now has %d spell points.\n", super.getName(), spellPoints);
		}
	}
	
	@Override
	public String toString() {
		return "Wizard " + super.toString() + "\n   Spell Points: " + spellPoints;
	
	}
}
