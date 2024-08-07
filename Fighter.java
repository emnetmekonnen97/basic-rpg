
/**
 * Name: Emnet Mekonnen
 * Date: 3/30/2024
 * CSC 202
 * Project 2--Fighter class
 *  
 * Description: This class models a Fighter character in a basic
 *              role playing game. Fighters have restricted armor
 *              and weapons, but after winning a certain number
 *              of fights can increase a level to receive additional
 *              armor and weapon choices
 * 
 * Citations of Assistance (who and what OR declare no assistance):
 * CodeHelp helped me correct a mistake in the fight method, I entered the fight method and asked
 * "When the code runs it prints out the statements from the super class twice"
 *
 */
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Fighter extends RPGCharacter {
	/**
	 * The initial and maximum number of health points for a Fighter
	 */
	public static final int MAX_HEALTH_POINTS = 40;
	
	/**
	 * When a Fighter wins his third fight, then he moves to level 2
	 */
	public static final int NUMBER_FIGHTS_WON_TO_LEVEL_UP = 3;
	
	/**
	 * The initial level for a Fighter with restricted armor and weapons
	 */
	public static final int INITIAL_LEVEL = 1;
	
	private int level;     // level of the character
	private int fightsWon;  // number of fights the character won
	
	/**
	 * Constructs a fighter by using the name and the max health points.
	 * Also initializes the level and number of fights won by the fighter
	 * @param name - name of the fighter
	 */
	public Fighter(String name) {
		super(name, MAX_HEALTH_POINTS);
		
		level = INITIAL_LEVEL;
		fightsWon = 0;
	}
	
	@Override
	public List<Action> getActionList(){
		List<Action> actionList = super.getActionList();
		if(getArmor() == Armor.NO_ARMOR) {
			actionList.add(0, Action.PUT_ON_ARMOR);
		}else {
			actionList.add(0, Action.REMOVE_ARMOR);
		}	
		return actionList;
	}
	
	@Override
	public List<Weapon> getAllowedWeaponList(){
		List<Weapon> weaponsList = new ArrayList<>();
		weaponsList.add(Weapon.DAGGER);
		weaponsList.add(Weapon.AXE);
		weaponsList.add(Weapon.STAFF);
		weaponsList.add(Weapon.SPEAR);
		
		if(level > INITIAL_LEVEL) {
			weaponsList.add(Weapon.SWARD);
			weaponsList.add(Weapon.WAR_HAMMER);
			
		}
		
		return weaponsList;
	}
	
	@Override
	public List<Armor> getAllowedArmorList(){
		List<Armor> armorList = new ArrayList<>();
		armorList.add(Armor.CHAIN);
		armorList.add(Armor.LEATHER);
		
		if(level > INITIAL_LEVEL) {
			armorList.add(Armor.PLATE);
		}
		
		return armorList;
	}
	
	/**
	 * adds armor to the character
	 * @param newArmor - the armor that is selected
	 */
	public void putOnArmor(Armor newArmor) {
		super.setArmor(newArmor);
		System.out.printf("%s now wearing %s armor.\n" , super.getName(), newArmor.getName());
	}
	
	/**
	 * removes the armor from the character
	 */
	public void takeOffArmor() {
		super.setArmor(Armor.NO_ARMOR);
		System.out.printf("%s no longer wearing armor.\n", super.getName());
	}
	
	@Override
	public boolean fight(RPGCharacter other) {
		boolean winsFight = super.fight(other);
		if(winsFight){
			fightsWon++;
		}
		
		if(fightsWon == NUMBER_FIGHTS_WON_TO_LEVEL_UP ) {
			level++;
		}
		
		return winsFight;
	
	}
	
	@Override
	public String toString() {
		return "Fighter " + super.toString() + "\n   Level: " + level  + "\n   Fights won: " + fightsWon;
	}

}
