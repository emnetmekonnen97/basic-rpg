/**
 * Name: Emnet Mekonnen 
 * Date: 3/16/2024
 * CSC 202
 * Project 2- RPGCharacter class
 * 
 * Description: This class models one character in a basic roll playing game
 * 
 * Citations of Assistance (who and what OR declare no assistance):
 * No assistance
 */
import java.util.ArrayList;
import java.util.List;

public abstract class RPGCharacter implements Chooseable{
	/**
	 * Number of sides for the die used in attacks
	 */
	public static final int NUMBER_SIDES_FOR_ATTACK = 12;
	
	/**
	 * The die with NUMBER_SIDES_FOR_ATTACK sides to be rolls when a fight
	 * ensues or a damaging spell is cast
	 */
	public static MultiSidedDie attackDie = new MultiSidedDie(NUMBER_SIDES_FOR_ATTACK);
	
	// Data fields
	private Weapon weapon;
	private String name;
	private int maxHealthPoints;
	private int currentHealth;
	private Armor armor;
	
	/**
	 * Constructs an RPGCharacter by using the name and maximum health points of the character
	 * @param name(String) - name of the character
	 * @param maxHealthPoints(int) - maximum amount of health points allowed for the character
	 */
	public RPGCharacter(String name, int maxHealthPoints) {
		this.name = name;
		this.maxHealthPoints = maxHealthPoints;
		this.currentHealth = maxHealthPoints;
		this.weapon = Weapon.HANDS;
		this.armor = Armor.NO_ARMOR;
	}
	
	/**
	 * Note: subclasses should override this method to provide a set of
	 * actions specific to that class.  Usually, the subclass should
	 * call RPGCharacter's getActionList() and prepend any additional
	 * character-specific actions to the front of it.
	 * @return list of actions currently available for this character
	 */
	public List<Action> getActionList() {
		List<Action> actions = new ArrayList<>();
		if (weapon == Weapon.HANDS) {
			actions.add(Action.WIELD);
		} else {
			actions.add(Action.UNWIELD);
		}
		actions.add(Action.FIGHT);
		actions.add(Action.CHECK_STATUS);
		actions.add(Action.END_TURN);
		return actions;
	}
	
	/**
	 * Retrieves a list of allowed weapons for the character
	 * 
	 * @return list containing the allowed weapons for that character
	 */
	public abstract List<Weapon> getAllowedWeaponList();
	
	/**
	 * Retrieves a list of allowed armor for the character
	 * 
	 * @return list containing the allowed armor for that character
	 */
	public abstract List<Armor> getAllowedArmorList();
	
	/**
	 * Retrieves the name of the character
	 * 
	 * @return String of the name of the character
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Retrieves the armor that the character is currently using 
	 * 
	 * @return the armor object that is currently being used by the character
	 */
	public Armor getArmor() {
		return armor;
	}
	
	/**
	 * Retrieves the weapon that the character is currently using
	 * 
	 * @return the weapon object that is currently being wielded by the character
	 */
	public Weapon getWeapon() {
		return weapon;
	}
	
	/**
	 * Retrieves the amount of health points the character has
	 * 
	 * @return the current health(int) of the character
	 */
	public int getHealthPoints() {
		return currentHealth;
	}
	
	/**
	 * Retrieves the maximum amount of health points the character can have
	 * 
	 * @return the maximum health points that the character could have
	 */
	public int getMaxHealthPoints() {
		return maxHealthPoints;
	}
	
	/**
	 * Changes the old armor to the new armor passed through the parameter 
	 * 
	 * @param newArmor(int) - the armor that the character will be putting on 
	 */
	public void setArmor(Armor newArmor) {
		armor = newArmor;
	}
	
	/**
	 * Changes the old health points to the new ones passed through the parameter
	 * 
	 * @param newHealthPoints(int) - the new amount of health points 
	 */
	public void setHealthPoints(int newHealthPoints) {
		if (newHealthPoints < 0 ){
		    currentHealth = 0;
		}else if (newHealthPoints > maxHealthPoints) {
			currentHealth =  maxHealthPoints;
		}else {
			currentHealth = newHealthPoints;
		}
	}
	
	/**
	 * Changes the weapon that the character will wield
	 * @param newWeapon(Weapon) - weapon that the character will wield
	 */
	public void wield(Weapon newWeapon) {
		weapon = newWeapon;
		System.out.printf("%s now wielding %s.\n", name, weapon.getName());
	}
	
	/**
	 * Unwields the weapon so that the characters weapon will be hands
	 */
	public void unwield() {
		weapon = Weapon.HANDS;
		System.out.printf("%s no longer wielding a weapon.\n", name);
	}
	
	/**
	 * checks if the character is defeated by checking if the current health is = 0
	 * @return True if the character is defeated, False if not.
	 */
	public boolean checkForDefeat() {
		return currentHealth == 0;
	}
	
	/**
	 * Generates a fight between the aggressor and defender by 
	 * rolling a die and checking if the die rolled is greater than the armor of the defender
	 * @param other - the character that is being fought 
	 * @return True if the characters armor is less than the attack die roll,
	 * 		   False if the die roll is less than the characters armor
	 */
	public boolean fight(RPGCharacter other) {
		attackDie.roll();
		if(attackDie.getValue() > other.getArmor().getArmorClass()) {
			other.currentHealth -= weapon.getDamagePoints();
			if(other.currentHealth < 0){
			    other.currentHealth = 0;
			}
			System.out.printf("Die rolled: %d\n", attackDie.getValue());
			System.out.printf("%s attacks %s with %s.\n", name, other.getName(), weapon.getName());
			System.out.printf("%s does %d damage to %s.\n", name, weapon.getDamagePoints(),other.getName());
			System.out.printf("%s now has %d health points.\n", other.getName(), other.getHealthPoints());
		}else {
			System.out.printf("Die rolled: %d\n", attackDie.getValue());
			System.out.printf("%s attacks %s with %s.\n", name, other.getName(), weapon.getName());
			System.out.printf("%s's armor defended him. No loss of health points.\n", other.getName());
		}
		return attackDie.getValue() > other.getArmor().getArmorClass();
	}
	
	/**
	 * Generates a string that contains the characters name, current health, weapon, armor 
	 */
	public String toString() {
		return String.format("%s\n   Current Health: %d\n   Wielding: %s\n   Wearing: %s", name, currentHealth, weapon.getName(), armor.getName());
		
	}
	
	
}
