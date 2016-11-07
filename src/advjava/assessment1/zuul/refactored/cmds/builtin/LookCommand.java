/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package advjava.assessment1.zuul.refactored.cmds.builtin;

import advjava.assessment1.zuul.refactored.Game;
import advjava.assessment1.zuul.refactored.InternationalisationManager;
import advjava.assessment1.zuul.refactored.character.Character;
import advjava.assessment1.zuul.refactored.cmds.Command;
import advjava.assessment1.zuul.refactored.cmds.CommandExecution;

/**
 * The look command reprints the rooms current information, or if a parameter is
 * specified it will try to find a character in the room to print details on
 * otherwise return an error to the player.
 * 
 * @author dja33
 */
public class LookCommand extends Command {

	public LookCommand(String name, String description) {
		super(name, description);
	}

	/**
	 * Print details about the current room, of a character if mentioned
	 */
	public boolean action(Game game, CommandExecution cmd) {

		// If the command length is greater than 1, we're looking for a
		// character
		// e.g "look Tom"
		if (cmd.commandLength() > 1) {

			String characterName = cmd.getWord(1);
			Character npc = game.getPlayer().getCurrentRoom().getCharacter(characterName);

			// If we found the character
			if (npc != null) {
				System.out.println(npc);
				return true;
			} else {
				// print an error, no character found
				System.out
						.println(String.format(InternationalisationManager.im.getMessage("look.noone"), characterName));
			}

			return false;

		} else {
			// print room details
			System.out.println(game.getPlayer().getCurrentRoom());
		}

		return true;
	}

}
