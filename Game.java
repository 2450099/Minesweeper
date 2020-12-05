/**
 *  IMPORTANT - Replace this entire header with an actual description of your game.
 *  
 *  Class Game - the main class of the adventure game
 *
 *  This class is the main class of a very simple text based adventure game.  
 *  Users can walk around some scenery. That's all. 
 *  It should really be modified to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play" method
 *  
 *  @author  Kolling/Aronson
 *  @version .1
 */

class Game 
{
    // These are the commands that are available.
    private final String INITIAL_COMMANDS = "go quit help back";
    private final String INITIAL_COMMANDS = "go quit help back items pick inventory drop exits";
    // This is the current room that that player is in
    private Room currentRoom;
    //This is the room the player was just in
@@ -25,9 +25,11 @@
    private String inventory;
    // tells if the game is over or not
    private boolean finished;
    // used to get rid of an itme in a room
    String tempItem = "";
    // These are references to the rooms that are available.  
    // The actual rooms are created in createRooms().
    private Room outside, lab, store, gym, office;  
    private Room outside, lab, store, gym, office, parking;  

    // This is the parser that deals with input.  You should not have to touch this.
    private Parser parser;
@@ -50,7 +52,7 @@ public Game()
    {
        createRooms();
        parser = new Parser(INITIAL_COMMANDS);
        inventory = "";
        inventory = " ";
        finished = false;
        image = new Image("images/lfhs.jpg");
    }
@@ -66,13 +68,18 @@ private void createRooms()
        store = new Room("the store");
        gym = new Room("the gym");
        office = new Room("the main office");
        parking = new Room("the parking lot");

        // initialise room exits
        outside.setExits(null, lab, gym, store);
        // room.setExits(N,E,S,w)
        outside.setExits(parking, lab, gym, store);
        outside.setItems(" dog ");
        lab.setExits(null, null, null, outside);
        store.setExits(null, outside, null, null);
        store.setItems(" money bottle ");
        gym.setExits(outside, office, null, null);
        office.setExits(null, null, null, gym);
        parking.setExits(null,null,outside,null);

        currentRoom = outside;  // start game outside
    }
@@ -122,13 +129,22 @@ private void processCommand(Command command)
        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        } else if (commandWord.equals("go"))
        {
        } else if (commandWord.equals("go")) {
            goRoom(command);
        } else if (commandWord.equals("quit")) {
            finished = true;  // signal that we want to quit
        } else if (commandWord.equals("back")) {
          goBack();
        } else if (commandWord.equals("items")) {
          printItems();
        } else if (commandWord.equals("pick")) {
          pickItem(command);
        } else if (commandWord.equals("inventory")) {
          printInventory();
        } else if (commandWord.equals("drop")) {
          dropItem(command);
        } else if (commandWord.equals("exits")) {
          System.out.println(currentRoom);
        } else {
            System.out.println("Command not implemented yet.");
        }
@@ -155,6 +171,7 @@ private void printHelp()
        System.out.println("Your command words are:");
        parser.showCommands();
    }


    /** 
     * Try to go to one direction. If there is an exit, enter the new
@@ -184,4 +201,66 @@ private void goRoom(Command command)
            System.out.println(currentRoom);
        }
    }
    /**
    *this moves an item from the room to your inventory
    */
    private void pickItem(Command command) {
      if (!command.hasSecondWord()) {
        // if there is no second word, we don't know what to pick up...
        System.out.println("Pick up what?");
        return;
      }
      String item = command.getSecondWord();
      //the code within the if statement is to ensure the game doesn't crash or act oddly if you input something that isn't an item in the room
      if (currentRoom.getItems().indexOf(item) > -1 && currentRoom.getItems().substring(currentRoom.getItems().indexOf(item) - 1, currentRoom.getItems().indexOf(item)).equals(" ") && currentRoom.getItems().substring(currentRoom.getItems().indexOf(item) + item.length(), currentRoom.getItems().indexOf(item) + item.length() + 1).equals(" ")) {
        tempItem = currentRoom.getItems();
        tempItem = tempItem.substring(0,tempItem.indexOf(item)) + tempItem.substring(tempItem.indexOf(item)+item.length());
        currentRoom.setItems(tempItem);
        inventory += item + " ";
        System.out.println("Picked up " + item);
      } else {
        System.out.println("That is not an item");
      }
    }
    /**
    *this lists the items in your inventory
    */
    public void printInventory() {
      if (inventory.trim().equals("")) {
        System.out.println("You have nothing in your inventory!");
      } else {
        System.out.println(inventory);
      }
    }
    /**
    *this moves an item from your inventory to the room
    */
    public void dropItem(Command command) {
      if (!command.hasSecondWord()) {
        // if there is no second word, we don't know what to pick up...
        System.out.println("Drop what?");
        return;
      }
      String item = command.getSecondWord();
        //the code within the if statement is to ensure the game doesn't crash or act oddly if you input something that isn't an item you have
      if (inventory.indexOf(item) > -1 && inventory.substring(inventory.indexOf(item) - 1, inventory.indexOf(item)).equals(" ") && inventory.substring(inventory.indexOf(item) + item.length(), inventory.indexOf(item) + item.length() + 1).equals(" ")) {
        tempItem = inventory;
        tempItem = tempItem.substring(0,tempItem.indexOf(item)) + tempItem.substring(tempItem.indexOf(item)+item.length());
        inventory = tempItem;
        currentRoom.setItems(currentRoom.getItems() + item + " ");
        System.out.println("Dropped " + item);
      } else {
        System.out.println("That is not an item");
      }
    }
    /**
    *this lists the items currently in the room
    */
    public void printItems() {
      if (currentRoom.getItems().trim().equals("")) {
        System.out.println("There are no items in this room!");
      } else {
        System.out.println(currentRoom.getItems());
      }
    }
}
