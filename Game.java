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
    private final String INITIAL_COMMANDS = "go quit help back items pick inventory drop exits observe map talk";
    // This is the current room that that player is in
    private Room currentRoom;
     //This is the room the player was just in
    private Room previousRoom;
    // This is the list of items that the person is carrying
    private String inventory;
    // tells if the game is over or not
    private boolean finished;
    // used to get rid of an item in a room
    String tempItem = "";
    //The weight of all things the player is holding
    int invWeight = 0; 
    // used to check if you can pick up an item
    int tempWeight = 0;
    // The max weight the player can carry
    final int MAX_CARRY_WEIGHT = 50;
    // Used to keep track of the player's progress
    int progress = 0;
    // These are references to the rooms that are available.  
    // The actual rooms are created in createRooms().
    private Room spawn, chess, wrench, store, gym, office, parking;   

    // This is the parser that deals with input.  You should not have to touch this.
    private Parser parser;
    // picture that is being shown
    private Image image;

    /**
     *  Create and play the game
     */
    public static void main(String args[]) 
    {
        Game game = new Game();
        game.play();
    }

    /**
     * Game constructor
     */
    public Game() 
    {
        createRooms();
        parser = new Parser(INITIAL_COMMANDS);
        inventory = " ";
        finished = false;
        image = new Image("images/lfhs.jpg");
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        // create the rooms
        spawn = new Room("the spawn");
        wrench = new Room("the room with the wrench");
        chess = new Room("the chess room");
        water_riddle = new Room("the store");
        water = new Room("the gym");
        ignorance_riddle = new Room("the main office");
        ignorance = new Room("the parking lot");
        wind_riddle = new Room("");
        wind = new Room("");
        victor_riddle = new Room("");
        trinity = new Room("");
        victory = new Room("");

        // initialise room exits
        // room.setExits(N,E,S,w)
        spawn.setExits(chess, null, wrench, null);
        chess.setExits(null, null, spawn, null);
        wrench.setExits(spawn, null, null, null);
        

        currentRoom = spawn;  // start game in spawn
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
        while (!finished)
        {
            Command command = parser.getCommand();
            processCommand(command);
        }
        System.out.println("Thank you for playing.  Goodbye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("The World has fallen into an age of darkness and despair.");
        System.out.println("Only you, the randomly chosen one, can restore balance to the world");
        System.out.println("Find the 4 essentials of life to bring the world back.");
        System.out.println();
        System.out.println(currentRoom);
    }

    /**
     * Given a command, process the command.
     * If this command ends the game, true is returned, otherwise false is returned.
     */
    private void processCommand(Command command) 
    {
        if(command.isUnknown())
        {
            System.out.println("I don't know what you mean...");
            return;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
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
        } else if (commandWord.equals("observe")) {
          printObservation();
        } else if (commandWord.equals("map")) {
          map();
        } else if (commandWord.equals("talk")) {
          talk();
        } else {
          System.out.println("Command not implemented yet.");
        }

    }
    private void goBack() {
      if (previousRoom != null) {
        currentRoom = previousRoom;
        System.out.println(currentRoom);
      } else {
        System.out.println("You can't go back!");
      }
    }
    /**
     * Print out some help information.
     * Here we print a cryptic message and a list of the 
     * command words.  Maybe you can improve on this.
     */
    private void printHelp() 
    {
        if (progress == 1) {
          System.out.println("You have a goal, but you're not sure how to go about achieving it. \nIf only there was somebody who could give you some advice...");
        }
        parser.showCommands();
    }

    /** 
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord())
        {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // See what the next room in that direction is
        Room nextRoom = currentRoom.nextRoom(direction);

        if (nextRoom == null) // there is no room in that direction
            System.out.println("There is no door!");
        else // go into the next room
        {
            // we just entered a new room 
            previousRoom = currentRoom;
            currentRoom = nextRoom;
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
        tempWeight = Integer.parseInt(item.substring(item.length()-2));
        if (invWeight + tempWeight < MAX_CARRY_WEIGHT) {
          tempItem = currentRoom.getItems();
          tempItem = tempItem.substring(0,tempItem.indexOf(item)) + tempItem.substring(tempItem.indexOf(item)+item.length());
          currentRoom.setItems(tempItem);
          inventory += item + " ";
          invWeight += tempWeight;
          System.out.println("Picked up " + item);
        } else {
          System.out.println("You have too much in your inventory to pick that up!");
        }
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
        tempWeight = Integer.parseInt(item.substring(item.length()-2));
        invWeight -= tempWeight;
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
    /**
    *this provides information about your surroundings
    */
    public void printObservation() {
      if (currentRoom.equals(wrench)) {
        System.out.println("You see the vine covered High School, as you stand on a grass lawn.  You are surrounded by some magical bubble that makes you go to weird places when you use the go command...");
      } else if (currentRoom.equals(parking)) {
        System.out.println("You get a car, you get a car, and YOUUUUU get a car!");
      } else {
        System.out.println("You don't see anything interesting...");
      }
    }
    //brings up map (if player has a map)
    public void map() {
      if (inventory.indexOf("map08") > -1) {
        System.out.println("Woah, you have a map, but you're illiterate!");
      } else {
        System.out.println("You don't have that map!");
      }
    }
    //player calls out and sees if anyone will respond
    public void talk() {
      if (currentRoom.equals(chess)) {
        if (inventory.indexOf("map08") > -1) {
          System.out.println("Bob: Hullo, there! What're ye doing here? \nBob: Saving the world, ye say? Sounds like ye need some help. \nBob: Ye got a map? Lemme have a look at it.  There ya go.");
          progress = 1;
          System.out.println("**Your map has been upgraded**");
          System.out.println("Bob: Come back to me when you find one of the four.");
        } else {
          System.out.println("Bob: Hullo, there! What're ye doing here? \nBob: Saving the world, ye say? Sounds like ye need some help. \nBob: Ye got a map? Hmm, go find a map and come back.");
        }
      } else {
        System.out.println("Your voice echoes off empty walls");
      }
    }
}
