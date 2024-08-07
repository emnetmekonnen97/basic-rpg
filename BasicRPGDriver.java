/**
 * Name: Emnet Mekonnen
 * Date: 4/01/2024
 * CSC 202
 * Project 2--BasicRPGDriver class
 *  
 * Description: This class is the executable class to play the basic
 *              role playing game. Users create the characters to play
 *              the game placing the characters in a random order.
 *              Then the characters can use weapons, armor and spells
 *              to try to defeat their enemies.
 * 
 * Citations of Assistance (who and what OR declare no assistance):
 * No assistance
 */

import java.util.*;

public class BasicRPGDriver {
	/**
	 * Minimum number of characters to play the game
	 */
	public static final int MIN_NUM_CHARACTERS = 2;

	/**
	 * The text to be used in the menu for choosing the type of character
	 */
	public static final TextChoice FIGHTER = new TextChoice("Fighter");
	public static final TextChoice WIZARD = new TextChoice("Wizard");

	/**
	 * The text be used in the menu for choosing whether to play another round
	 */
	public static final TextChoice YES_ANSWER = new TextChoice("Yes");
	public static final TextChoice NO_ANSWER = new TextChoice("No");

	public static void main(String[] args){
		Scanner console = new Scanner(System.in);

		System.out.println("Let's play Basic RPG\n");
		List<RPGCharacter> characterList = initializeCharacters(console);
		Collections.shuffle(characterList);
		
//		List<RPGCharacter> characterList = new ArrayList<>();
//		 characterList.add(new Wizard("Willy"));
//		 characterList.add(new Fighter("Fred"));
//		 characterList.add(new Fighter("Felix"));
//		 characterList.add(new Wizard("Walt"));
		printCharacterOrder(characterList);
		

		TextChoice keepPlaying = YES_ANSWER;
		do {
			int index = 0;
			while (index < characterList.size()) {
				RPGCharacter currentPlayer = characterList.get(index);
				takeOneTurn(characterList, currentPlayer, console);
				index = characterList.indexOf(currentPlayer);
				index++;
			}

			System.out.println("Another round? ");
			keepPlaying = showMenu(console, Arrays.asList(YES_ANSWER, NO_ANSWER));
		} while (keepPlaying == YES_ANSWER);

		System.out.println("Living characters at end of game: ");
		for (RPGCharacter character : characterList) {
			System.out.println(character.getName());
		}
		System.out.println("\nThanks for playing!");

	}
	
	/**
	 * Compares the name entered by player to all the other names in the character list
	 * @param name - name of the character 
	 * @param characterList(RPGCharacter) - the ordered list of characters for the game
	 * @return true if the name of another character already exists in the list
	 * 		   false if otherwise
	 */
	private static boolean checkIfNameExists(String name, List<RPGCharacter> characterList) {
		for(int i = 0; i < characterList.size(); i++) {
			if(characterList.get(i).getName().equals(name)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Allows the user to enter characters choosing between the types
	 * @param console - facilitates keyboard input
	 * @return a list of the characters for this role playing game
	 */
	public static List<RPGCharacter> initializeCharacters(Scanner console) {
		System.out.println("Create your characters.\n");
		List<RPGCharacter> characterList = new ArrayList<>();

		TextChoice keepAddingCharacters;
		do {
			System.out.println("Choose character type.");
			TextChoice classChoice = showMenu(console, Arrays.asList(FIGHTER, WIZARD));
			System.out.println("Enter the character's name: ");
			String name = console.nextLine();
			
			while(checkIfNameExists(name, characterList)) {
				System.out.println("Invalid name. Character already exists.");
				System.out.println("Enter the character's name: ");
				name = console.nextLine();
			}
			if (classChoice == FIGHTER) {
				characterList.add(new Fighter(name));
			} else {
				characterList.add(new Wizard(name));
			}

			System.out.println("\nAnother character?");
			keepAddingCharacters = showMenu(console, Arrays.asList(YES_ANSWER, NO_ANSWER));
			if (keepAddingCharacters == NO_ANSWER && characterList.size() < MIN_NUM_CHARACTERS) {
				System.out.println("You need at least two characters. Make a character.");
				keepAddingCharacters = YES_ANSWER;
			}
		} while (keepAddingCharacters == YES_ANSWER);

		return characterList;
	}

	/**
	 * Prints the list of characters in the order that they will take turns
	 * @param characterList-the ordered list of characters for the game
	 */
	public static void printCharacterOrder(List<RPGCharacter> characterList) {
		System.out.print("Order of play: " + characterList.get(0).getName());
		for (int i = 1; i < characterList.size(); i++) {
			System.out.print(", " + characterList.get(i).getName());
		}
		System.out.println("\n");
	}

	/**
	 * Allows one character to take one turn. The menu at each stage of the turn will display
	 * only the choices the character can make at that stage.
	 * 
	 * @param characterList - the current list of characters for this role playing game
	 * @param currentPlayer - the character currently taking their turn
	 * @param console - facilitates keyboard input
	 */
	public static void takeOneTurn(List<RPGCharacter> characterList, RPGCharacter currentPlayer, Scanner console) {

		List<Action> completedOncePerTurnActions = new ArrayList<>();
		Action choice;
		do {
			List<Action> availableActions = createAvailableActionsList(completedOncePerTurnActions, currentPlayer);
	
			System.out.println("\n" + currentPlayer.getName() + ", what do you want to do? ");
			choice = showMenu(console, availableActions);

			if (choice == Action.PUT_ON_ARMOR) {
				System.out.printf("%s which armor?\n", currentPlayer.getName());
				Armor armorChoice = showMenu(console, currentPlayer.getAllowedArmorList());
				((Fighter) (currentPlayer)).putOnArmor(armorChoice);	
			} else if (choice == Action.REMOVE_ARMOR) {
				((Fighter) (currentPlayer)).takeOffArmor();	
			} else if (choice == Action.WIELD) {
				System.out.printf("%s which weapon?\n", currentPlayer.getName());
				Weapon weaponChoice = showMenu(console, currentPlayer.getAllowedWeaponList());
				currentPlayer.wield(weaponChoice);
			} else if (choice == Action.UNWIELD) {
				currentPlayer.unwield();
			} else if (choice == Action.FIGHT) {
				fight(currentPlayer, characterList, console);
			} else if (choice == Action.CAST_SPELL) {
				castSpell(((Wizard)(currentPlayer)), characterList, console);
			} else if (choice == Action.REGAIN_SPELL_POINTS) {
				((Wizard) (currentPlayer)).regainSpellPoints();
			} else if (choice == Action.CHECK_STATUS) {
				System.out.println(currentPlayer);
			}
			updateOncePerTurnActionsList(choice, completedOncePerTurnActions, availableActions);
		} while (choice != Action.END_TURN);
	}
	
	/**
	 * Creates the list of actions available to the current character at this stage of their turn
	 * @param completedOncePerTurnActions - actions that can only be completed once in the current players turn
	 * @param currentPlayer - the character currently talking their turn
	 * @return the list of actions available to the current character at this stage of their turn
	 */
	public static List<Action> createAvailableActionsList(List<Action> completedOncePerTurnActions, RPGCharacter currentPlayer){
		List<Action> availableActions = new ArrayList<>();
		for (Action action : currentPlayer.getActionList()) {
			if (!completedOncePerTurnActions.contains(action)) {
				availableActions.add(action);
			}
		}
		return availableActions;
	}
	/**
	 * Updates the list of actions that can be taken once per turn depending on the action the character has already taken
	 * @param choice - the current player's most recent action choice
	 * @param completedOncePerTurnActions - the list that will be updated to have the actions that can currently be
	 * 		taken once per turn
	 * @param availableActions - the actions available to this character
	 */
	public static void updateOncePerTurnActionsList(Action choice, List<Action> completedOncePerTurnActions, List<Action> availableActions) {
		if (choice.isOnlyOncePerTurn()) {
			for (Action action : availableActions) {
				if (action.isOnlyOncePerTurn()) {
					completedOncePerTurnActions.add(action);
				}
			}
		}
	}

	/**
	 * Allows a character to choose who they will fight and enacts the fight removing the
	 * character from the game if they are defeated
	 * @param aggressor - the character initiating the fight
	 * @param characterList - the list of characters currently active in the game
	 * @param console - facilitates keyboard input
	 */
	public static void fight(RPGCharacter aggressor, List<RPGCharacter> characterList, Scanner console) {
		System.out.printf("%s, who do you want to fight?\n", aggressor.getName());
		List<RPGCharacter> listWithoutAggressor = new ArrayList<>();
		listWithoutAggressor.addAll(characterList);
		listWithoutAggressor.remove(aggressor);
		RPGCharacter character = showMenu(console, listWithoutAggressor);
		aggressor.fight(character);
		
		if(character.checkForDefeat()) {
				characterList.remove(character);
		}
		
		
		
	}

	/**
	 * Allows a wizard to choose a spell and on which character they will cast the spell. Damaging
	 * spells can be cast on any other character. The wizard can cast the healing spell on himself
	 * or any other character. If the receiver of the spell is defeated, they are removed from the game.
	 * 
	 * @param wizard - the character that will cast the spell
	 * @param characterList - the list of characters currently active in the game
	 * @param console - facilitates keyboard input
	 */
	public static void castSpell(Wizard wizard, List<RPGCharacter> characterList, Scanner console) {
		System.out.printf("%s, which spell do you want to cast?\n", wizard.getName());
		Spell spellChoice = showMenu(console, Spell.ALL_SPELL_LIST);
		System.out.printf("%s, who is the target of the spell?\n", wizard.getName());
		
		List<RPGCharacter> listWithoutAggressor = new ArrayList<>();
		listWithoutAggressor.addAll(characterList);
		
		if(!spellChoice.getName().equals(Spell.HEAL.getName())) {
			listWithoutAggressor.remove(wizard);
		}
		RPGCharacter target = showMenu(console, listWithoutAggressor);
		wizard.castSpell(spellChoice, target);
		
		if(wizard.checkForDefeat()) {
			characterList.remove(wizard);
		}
	}

	/**
	 * prints the menu and gets a valid choice from the user
	 * 
	 * @param console - facilitates keyboard input
	 * @param items - the items used to populate the menu
	 * @return the item chosen by the user
	 */
	// Note this method is using a generic type allowing its flexible use with any time
	// extending Chooseable. So if the user is to choose a weapon from the menu, the type
	// returned is Weapon, but if the user if to choose a spell from the menu,
	// the type returned is Spell.
	public static <T extends Chooseable> T showMenu(Scanner console, List<T> items) {
		for (int num = 0; num < items.size(); num++) {
			System.out.println("(" + (num + 1) + ") " + items.get(num).getName());
		}
		System.out.print("Choice? ");
		int choice = 0;
		boolean choiceValid = false;
		do {
			try {
				choice = console.nextInt();
				if (choice >= 1 && choice <= items.size()) {
					choiceValid = true;
				} else {
					System.out.println("Invalid choice. Try again.");
					System.out.print("Choice? ");
				}
			} catch (InputMismatchException e) {
				console.nextLine();
				System.out.println("Invalid choice. Try again.");
				System.out.print("Choice? ");
			}
		} while (!choiceValid);
		console.nextLine();
		System.out.println();
		return items.get(choice - 1);
	}

}
