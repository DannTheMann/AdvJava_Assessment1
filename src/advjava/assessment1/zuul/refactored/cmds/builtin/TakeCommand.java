package advjava.assessment1.zuul.refactored.cmds.builtin;

import advjava.assessment1.zuul.refactored.Game;
import advjava.assessment1.zuul.refactored.InternationalisationManager;
import advjava.assessment1.zuul.refactored.Item;
import advjava.assessment1.zuul.refactored.Room;
import advjava.assessment1.zuul.refactored.character.Player;
import advjava.assessment1.zuul.refactored.cmds.Command;
import advjava.assessment1.zuul.refactored.cmds.CommandExecution;

/**
 * 
 * The Take command is used when a player wants to pickup an item from the
 * current room they're in. If the player can't pick up the item because it
 * exceeds their maximum weight or doesn't exist then an error will be printed
 * to the player.
 * 
 * @author dja33
 *
 */
public class TakeCommand extends Command {

	public TakeCommand(String name, String description) {
		super(name, description);
	}

	/**
	 * Try to pickup an item from the current room, if it doesn't exit then
	 * print an error or if the item exceeds the maximum weight of the player
	 */
	@Override
	public boolean action(Game game, CommandExecution cmd) {

		// If the command length is greater than 1, we have the correct command
		// e.g "pickupitem apple"
		if (cmd.commandLength() > 1) {

			String itemName = cmd.getWord(1);

			Room room = game.getPlayer().getCurrentRoom();

			// If the room has the item mentioned
			if (room.hasItem(itemName)) {

				// Get the item
				Item item = room.getItem(itemName);
				Player player = game.getPlayer();

				// Check whether the player can add this item without
				// becoming over encumbered
				if (player.getWeight() + item.getWeight() > player.getMaxWeight()) {
					// Player is over encumbered
					System.out.println(String.format(InternationalisationManager.im.getMessage("pickup.heavy"), item,
							System.lineSeparator(), game.getPlayer().getWeight()));
					return true;
				}

				// Add the item
				player.addItem(item);
				room.removeItem(item);
				// Update weights
				player.setWeight(player.getWeight() + item.getWeight());

				System.out.println(String.format(InternationalisationManager.im.getMessage("pickup.success"), item));

				return true;
			} else {
				// No item exists in this room called that
				System.out.println(String.format(InternationalisationManager.im.getMessage("pickup.noone"), itemName));
			}

		} else {
			// Not enough params
			System.out.println(InternationalisationManager.im.getMessage("pickup.noparam"));
		}

		return false;
	}

}
