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
    private final String INITIAL_COMMANDS = "go quit help back items pick inventory drop exits observe map talk debug answer";
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
    private Room spawn, chess, wrench, water_riddle, water, ignorance_riddle, ignorance, wind_riddle, wind, victor_riddle, trinity, victory;

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
        spawn.setItems(" map08 ");
        chess.setExits(null, null, spawn, null);
        wrench.setExits(spawn, null, null, null);
        wrench.setItems(" wrench12 ");
        water_riddle.setExits(null, null, null, chess);
        water.setExits(null, null, null, water_riddle);
        water.setItems(" water_stone12 ");
        ignorance_riddle.setExits(water_riddle, null, null, null);
        ignorance.setExits(ignorance_riddle, null, null, null);
        ignorance.setItems(" ignorance_stone12 ");
        wind_riddle.setExits(null, null, chess, null);
        wind.setExits(null, null, null, wind_riddle);
        wind.setItems(" wind_stone12 ");
        victor_riddle.setExits(null, chess, null, null);
        trinity.setExits(null, victor_riddle, null, null);
        victory.setExits(null, null, trinity, null);

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
        } else if (commandWord.equals("debug")) {
          progress++;
        } else if (commandWord.equals("answer")) {
          answer(command);
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
        System.out.println("You are in a room with white walls and harsh flourescent lights.\nThere is a reflective tiled floor beneath your feet, and something lying on the floor.\nThere is a sign on the far wall that reads: \"Made by Connor K\"");
      } else if (currentRoom.equals(spawn)) {
        System.out.println("You are in a dark room with a vaulted ceiling and a rough stone floor.\nThere is something on the far wall.");
      } else if (currentRoom.equals(chess)) {
        System.out.println("There are a few chess boards scattered throughout the room on small tables.\nThere is a man sitting at one of these tables, examining a board.");
      } else if (currentRoom.equals(water_riddle)) {
        System.out.println("You are in a large lavishly furnished room with a fountain in the middle.\nThere is graffiti on the far wall.  It reads \"When the heavens cry, millions of us will come.  What am I?\"");
      } else if (currentRoom.equals(water)) {
        System.out.println("A large room with white marble columns reaching up to the ceiling.\nIn the middle is a pedestal with something on it.");
      } else if (currentRoom.equals(ignorance_riddle)) {
        System.out.println("You find yourself looking at an 18th century style sitting room.\nOn one wall is a portrait of bananas.\nOn the other is red writing that reads \"The greatest folly of man is knowledge.  Ignorance is ...\"");
      } else if (currentRoom.equals(ignorance)) {
        System.out.println("You are in a pitch black room.\nNothing is visible except a pedestal in the middle, with an item resting atop it.");
      } else if (currentRoom.equals(wind_riddle)) {
        System.out.println("The walls are covered in a pattern of white and grey swirls, while the ceiling is a beautiful depiction of a storm.\n Some of the clouds make letters, and they read \"I move through the alphabet, stealing names.\nI am a mixture of wind, rain, and clouds.\nI cause destruction wherever I go.\"");
      } else if (currentRoom.equals(wind)) {
        System.out.println("Despite no apparent doors or windows aside from the one you used, you feel a slight breeze.\nThere is a pedestal in the middle of the room with something resting on it.");
      } else if (currentRoom.equals(victor_riddle)) {
        System.out.println("You are in a room covered in paintings.  There are depictions of battles, chariot races, and coloseums. \nOn one such painting, there is a caption that reads \"Once I was a greek goddess, now I am a shoe.  Swoosh.\"");
      } else if (currentRoom.equals(trinity)) {
        System.out.println("You are in a room with 3 glowing pedestals of red, green, and blue.\nConnecting them are glowing lines on the floor.\nThe pedestals appear to have an indent, as if something should be placed on them...");
      } else if (currentRoom.equals(victory)) {
        System.out.println("You are in a comfortable room with a sofa, a water cooler, and a television.\nThere are 4 men in suits on the couch, and the TV appears to be displaying rooms similar to the ones you went through.");
      } else {
        System.out.println("You don't see anything interesting...");
      }
    }
    //brings up map (if player has a map)
    public void map() {
      if (inventory.indexOf("map08") > -1) {
        if (progress == 0) {
          image = new Image("images/map1.png");
        } else if (progress == 1) {
          image = new Image("images/map2.png");
        } else if (progress == 2) {
          image = new Image("images/map3.png");
        } else if (progress == 3) {
          image = new Image("images/map4.png");
        } else if (progress == 4) {
          image = new Image("images/map5.png");
        }
      } else {
        System.out.println("You don't have that map!");
      }
    }
    //player calls out and sees if anyone will respond
    public void talk() {
      if (currentRoom.equals(chess)) {
        if (inventory.indexOf("map08") > -1) {
          System.out.println("Bob: Hullo, there! What're ye doing here? \nBob: Saving the world, ye say? Sounds like ye need some help. \nBob: Ye got a map? Lemme have a look at it.  There ya go.");
          if (progress == 0) progress = 1;
          chess.setExits(null, water_riddle, spawn, null);
          System.out.println("**Your map has been upgraded**");
          System.out.println("**A door has opened!**");
          System.out.println("Bob: Come back to me when you find one of the four.");
        } else {
          System.out.println("Bob: Hullo, there! What're ye doing here? \nBob: Saving the world, ye say? Sounds like ye need some help. \nBob: Ye got a map? Hmm, go find a map and come back.");
        }
      } else {
        System.out.println("Your voice echoes off empty walls");
      }
    }
    //player attempts to answer a riddle
    public void answer(Command command) {
      if (!command.hasSecondWord()) {
        System.out.println("What is your answer?");
        return;
      } else {
        String answer = command.getSecondWord();
        if (currentRoom.equals(water_riddle) && answer.equals("rain")) {
          System.out.println("You are correct!");
          if (progress == 1) {
            progress++;
            water_riddle.setExits(null, water, null, chess);
            chess.setExits(wind_riddle, water_riddle, spawn, null);
            System.out.println("**A door has opened!**");
          }
        } else if (currentRoom.equals(wind_riddle) && answer.equals("hurricane")) {
            System.out.println("You are correct!");
            if (progress == 2) {
              progress++;
              wind_riddle.setExits(null, wind, chess, null);
              water_riddle.setExits(null, water, ignorance_riddle, chess);
              System.out.println("**A door has opened!**");
            }
        } else if (currentRoom.equals(ignorance_riddle) && answer.equals("bliss")) {
            System.out.println("You are correct!");
            if (progress == 3) {
              progress++;
              ignorance_riddle.setExits(water_riddle, null, ignorance, null);
              chess.setExits(wind_riddle, water_riddle, spawn, victor_riddle);
              System.out.println("A door has opened!");
            }
        } else if (currentRoom.equals(victor_riddle) && answer.equals("nike")) {
            System.out.println("You are correct!");
            if (progress == 4) {
              victor_riddle.setExits(null, chess, null, trinity);
              System.out.println("A door has opened!");
            }
        } else {
          System.out.println("You are wrong, or there is no riddle to answer.");
        }
      }
    }
}
