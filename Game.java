/**
 *  World Saver - Ep. 1
 *  
 *  Class Game - the main class of the adventure game
 *
 *  This class is the main class of text based adventure game.  
 *  The user must seek Bob's help in collecting the three essentials of human life - water, wind, and ignorance - and then complete the ritual to save the world.
 * 
 *  Talk to people, move between rooms, check an updating map, pick up and drop items, look around a room, and so much more!
 *  
 *
 *  
 *  @author  Kolling/Aronson
 *  @author Koefelda
 *  @version 1.0
 *  BELOW IS A PLAYTHROUGH
 *  BELOW ARE SPOILERS
 *  1. Pick up the map in the spawn
 *  2. Go north and talk to Bob
 *  3. Go east, give the answer to the riddle (rain)
 *  4. Go east and get both the stone and the water
 *  5. Go west twice, then north
 *  6. Give the answer to the riddle (hurricane)
 *  7. Go east and get the wind stone and the first aid kit
 *  8. Go west, south, east, and south
 *  9. Give the answer to the riddle (bliss)
 *  10. Go south and get the ignorance stone and the food04
 *  11. Go north twice, go west
 *  12. Talk to Bob
 *  13. Answer Bob's questions (Yes, (any), Yes)
 *  14. Go west
 *  15. Answer the riddle (nike)
 *  16. Go west
 *  17. Complete the ritual (drop 3 stones, ritual command)
 *  18. Go north and talk to the men in suits
 */

class Game 
{
    // These are the commands that are available.
    private final String INITIAL_COMMANDS = "answer back drop exits go help inventory items map observe pick quit talk ritual";
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
    final int MAX_CARRY_WEIGHT = 58;
    // Used to keep track of the player's progress
    int progress = 0;
    //Used to keep track of Bob's questions
    int bobQuestion = 0;
    //Used to keep track of how many times a player uses help during a certain phase of the game
    int desparation0 = 0;
    int desparation1 = 0;
    int desparation2 = 0;
    int desparation3 = 0;
    int desparation4 = 0;
    int desparation6 = 0;
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
        image = new Image("images/spawn.jpg");
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
        water_riddle = new Room("the room with the water riddle");
        water = new Room("the room of the water stone");
        ignorance_riddle = new Room("the room with the ignorance riddle");
        ignorance = new Room("the room with the ignorance stone");
        wind_riddle = new Room("the room with the wind riddle");
        wind = new Room("the room with the wind stone");
        victor_riddle = new Room("the room with the final riddle");
        trinity = new Room("the ritual room");
        victory = new Room("the room of victors");

        // initialise room exits
        // room.setExits(N,E,S,w)
        spawn.setExits(chess, null, wrench, null);
        spawn.setItems(" map08 halberd08 ");
        chess.setExits(null, null, spawn, null);
        chess.setItems(" chess02 dagger05 ");
        wrench.setExits(spawn, null, null, null);
        wrench.setItems(" wrench12 bananas02 ");
        water_riddle.setExits(null, null, null, chess);
        water.setExits(null, null, null, water_riddle);
        water.setItems(" water_stone12 water04 ");
        ignorance_riddle.setExits(water_riddle, null, null, null);
        ignorance.setExits(ignorance_riddle, null, null, null);
        ignorance.setItems(" ignorance_stone12 food04 ");
        wind_riddle.setExits(null, null, chess, null);
        wind.setExits(null, null, null, wind_riddle);
        wind.setItems(" wind_stone12 firstaid04 ");
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
        System.out.println("Find the 3 essentials of life to bring the world back.");
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
        } else if (commandWord.equals("answer")) {
          answer(command);
        } else if (commandWord.equals("ritual")) {
          ritual();
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
        if (progress == 0 && desparation0 == 0) {
          System.out.println("You have a goal, but you're not sure how to go about achieving it. \nIf only there was somebody who could give you some advice...");
          desparation0 = 1;
        } else if (progress == 0 && desparation0 == 1){
          System.out.println("Talk to Bob in the chess room.");
        } else if (progress == 1 && desparation1 == 0) {
          System.out.println("Do what Bob tells you.");
          desparation1 = 1;
        } else if (progress == 1 && desparation1 == 1) {
          System.out.println("Complete the water riddle.");
        } else if (progress == 2 && desparation2 == 0) {
          System.out.println("Do as Bob tells you.");
          desparation2 = 1;
        } else if (progress == 2 && desparation2 == 1) {
          System.out.println("Complete the wind riddle.");
        } else if (progress == 3 && desparation3 == 0) {
          System.out.println("Do what Bob asks of you.");
          desparation3 = 1;
        } else if (progress == 3 && desparation3 == 1) {
          System.out.println("Complete the ignorance riddle.");
        } else if (progress == 4 && desparation4 == 0) {
          System.out.println("Talk to Bob.");
          desparation4 = 1;
        } else if (progress == 4 && desparation4 == 1) {
          System.out.println("Bring the 3 stones to Bob in chess");
        } else if (progress == 5 && desparation6 == 0) {
          System.out.println("Complete the ritual.");
          desparation6++;
        } else if (progress == 5 && desparation6 == 1) {
          System.out.println("Drop the 3 stones in the ritual room");
        } else if (progress == 6) {
          System.out.println("Talk to the men in the victory room");
        }
        System.out.println("COMMANDS:");
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
            //changes image to room entered
            if (currentRoom.equals(wrench)) {
              image.setImage("images/wrench.jpg");
            } else if (currentRoom.equals(spawn)) {
              image.setImage("images/spawn.jpg");
            } else if (currentRoom.equals(chess)) {
              image.setImage("images/chess.jpg");
            } else if (currentRoom.equals(water_riddle)) {
              image.setImage("images/water_riddle.jpg");
            } else if (currentRoom.equals(water)) {
              image.setImage("images/water.jpg");
            } else if (currentRoom.equals(ignorance_riddle)) {
              image.setImage("images/ignorance_riddle.jpg");
            } else if (currentRoom.equals(ignorance)) {
              image.setImage("images/ignorance.jpg");
            } else if (currentRoom.equals(wind_riddle)) {
              image.setImage("images/wind_riddle.jpg");
            } else if (currentRoom.equals(wind)) {
              image.setImage("images/wind.jpg");
            } else if (currentRoom.equals(victor_riddle)) {
              image.setImage("images/victor_riddle.jpg");
            } else if (currentRoom.equals(trinity)) {
              image.setImage("images/trinity.jpg");
            } else if (currentRoom.equals(victory)) {
              image.setImage("images/victory.jpg");
            }
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
        System.out.println("You are in a room with white walls and harsh flourescent lights.\nThere is a reflective tiled floor beneath your feet, and something lying on the floor.\nThere is a sign on the far wall that reads: \n\"Made by Connor K\"");
      } else if (currentRoom.equals(spawn)) {
        System.out.println("You are in a dark room with a vaulted ceiling and a rough stone floor.\nThere is something on the far wall.");
      } else if (currentRoom.equals(chess)) {
        System.out.println("There are a few chess boards scattered throughout the room on small tables.\nThere is a man sitting at one of these tables, examining a board.");
      } else if (currentRoom.equals(water_riddle)) {
        System.out.println("You are in a large lavishly furnished room with a fountain in the middle.\nThere is graffiti on the far wall.  It reads \n\"When the heavens cry, millions of us will come.  What am I?\"");
      } else if (currentRoom.equals(water)) {
        System.out.println("A large room with white marble columns reaching up to the ceiling.\nIn the middle is a pedestal with something on it.");
      } else if (currentRoom.equals(ignorance_riddle)) {
        System.out.println("You find yourself looking at an 18th century style sitting room.\nOn one wall is a portrait of bananas.\nOn the other is red writing that reads \n\"The greatest folly of man is knowledge.  Ignorance is ...\"");
      } else if (currentRoom.equals(ignorance)) {
        System.out.println("You are in a pitch black room.\nNothing is visible except a pedestal in the middle, with an item resting atop it.");
      } else if (currentRoom.equals(wind_riddle)) {
        System.out.println("The walls are covered in a pattern of white and grey swirls, while the ceiling is a beautiful depiction of a storm.\n Some of the clouds make letters, and they read \n\"I move through the alphabet, stealing names.\nI am a mixture of wind, rain, and clouds.\nI cause destruction wherever I go.\"");
      } else if (currentRoom.equals(wind)) {
        System.out.println("Despite no apparent doors or windows aside from the one you used, you feel a slight breeze.\nThere is a pedestal in the middle of the room with something resting on it.");
      } else if (currentRoom.equals(victor_riddle)) {
        System.out.println("You are in a room covered in paintings.  There are depictions of battles, chariot races, and coloseums. \nOn one such painting, there is a caption that reads \n\"Once I was a greek goddess, now I am a shoe.  Swoosh.\"");
      } else if (currentRoom.equals(trinity)) {
        System.out.println("You are in a room with 3 glowing pedestals of red, green, and blue.\nConnecting them are glowing lines on the floor.\nThe pedestals appear to have an indent, as if something should be placed on them...");
      } else if (currentRoom.equals(victory)) {
        System.out.println("You are in a comfortable room with a sofa, a water cooler, and a television.\nThere are 4 men in suits on the couch, and the TV appears to be displaying rooms similar to the ones you went through.");
      } else {
        System.out.println("You don't see anything interesting...");
      }
    }
    /**
    *brings up map (if player has a map)
    */
    public void map() {
      if (inventory.indexOf("map08") > -1) {
        if (progress == 0) {
          image.setImage("images/map1.png");
        } else if (progress == 1) {
          image.setImage("images/map2.png");
        } else if (progress == 2) {
          image.setImage("images/map3.png");
        } else if (progress == 3) {
          image.setImage("images/map4.png");
        } else if (progress == 4) {
          image.setImage("images/map5.png");
        }
        System.out.println("You opened your map!");
      } else {
        System.out.println("You don't have that map!");
      }
    }
    /**
    *player calls out and sees if anyone will respond
    */
    public void talk() {
      if (currentRoom.equals(chess)) {
        if (progress == 0 || progress == 1) {
          if (inventory.indexOf("map08") > -1) {
            System.out.println("Bob: Hullo, there! What're ye doing here? \nBob: Saving the world, ye say? Sounds like ye need some help. \nBob: Ye got a map? Lemme have a look at it.  There ya go.");
            if (progress == 0) progress = 1;
            chess.setExits(null, water_riddle, spawn, null);
            System.out.println("**Your map has been upgraded**");
            System.out.println("**A door has opened!**");
            System.out.println("Bob: Come back to me when you find one of the three.");
          } else {
            System.out.println("Bob: Hullo, there! What're ye doing here? \nBob: Saving the world, ye say? Sounds like ye need some help. \nBob: Ye got a map? Hmm, go find a map and come back.");
          }
        } else if (progress == 2) {
          if (inventory.indexOf("water_stone12") > -1) {
            System.out.println("Bob: Ah, I see you have the water stone.\nGo try to another of the three will you?");
          } else {
            System.out.println("Bob: Hm, you don't have the water stone?  It should have been in the room after the riddle...\nBob: Go get it.");
          }
        } else if (progress == 3) {
          if (inventory.indexOf("wind_stone12") > -1) {
            System.out.println("Bob: You got the wind stone!  I never could figure out that dumb riddle.\nGo get the last of the three, will you?");
          } else {
            System.out.println("Bob: Huh, why don't you have the wind stone?  It should have been in the room after the riddle... \nBob: Go get it for me.");
          }
        } else if (progress == 4) {
          if (inventory.indexOf("water_stone12") > -1 && inventory.indexOf("wind_stone12") > -1 && inventory.indexOf("ignorance_stone12") > -1) {
            System.out.println("Bob: Hey, you got all the stones!\nBob: Now, I'm very proud of you for succeeding where I've failed all these years.\nBob: Unforunately, I'm going to have to take those from you now.\nBob: To think I've wasted all this time, just to have somebody show up and hand me the stones!\nBob: Oh, don't look so distressed, anyone would have been as easy to trick as you.\nBob: Alright, how about a game.  I give you three questions, and if you answer them all, you can pass.\nBob: Oh, right, the door's been right behind me this whole time, I just needed the stones. \nBob: Alright, first question: Is the world really worth saving?");
            bobQuestion = 1;
          } else {
            System.out.println("Bob: Why don't you have all the stones?\nBob: Bring them here and I'll tell you how to finish.  Hurry up.");
          }
        }
      } else if (currentRoom.equals(victory)) {
        if (inventory.indexOf("food04") > -1 && inventory.indexOf("water04") > -1 && inventory.indexOf("firstaid04") > -1) {
          System.out.println("Suit1: Congratulations on making it out.  You passed the test in front of you, but also have the skills to survive.\nSuit2: Had you come out with nothing, or useless junk, you would have been useless to us.\nSuit3: But since you came out with what you needed to survive in the case of a prolonged journey, it's time for your training.");
          System.out.println("**Screen fades to black, followed by epic training arc to save the world**");
          System.out.println("You beat the game!");
          finished = true;
        } else {
          System.out.println("Suit1: Good job on making it out, and completing the ritual.\nSuit2: Unforunately, you only see what's in front of you.\nSuit3: Our candidates are expected to prepare themselves for what's to come if they are going to work with us.  You didn't.\nSuit4: As such, we no longer have need of you.  See yourself out.");
          System.out.println("**You are given the boot with nowhere to go**");
          System.out.println("You finished, but did you realy win?");
          finished = true;
        }
      } else {
        System.out.println("Your voice echoes off empty walls");
      }
    }
    /**
    *player attempts to answer a riddle
    */
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
              chess.setExits(wind_riddle, water_riddle, spawn, null);
              System.out.println("A door has opened!");
            }
        } else if (currentRoom.equals(victor_riddle) && answer.equals("nike")) {
            System.out.println("You are correct!");
            if (progress == 4) {
              victor_riddle.setExits(null, chess, null, trinity);
              progress++;
              System.out.println("A door has opened!");
            }
        } else if (currentRoom.equals(chess) && bobQuestion > 0) {
          if (bobQuestion == 1) {
            if (answer.equals("yes") || answer.equals("y")) {
              bobQuestion = 2;
              System.out.println("Bob: HAH, wrong, but I suppose that's opinion.\nBob: Here's your second question: What is the best word in the english  dictionary?");
            } else if (answer.equals("no") || answer.equals("n")) {
              System.out.println("Bob: Correct, but I suppose there's no reason for me to give you the stones then...");
              System.out.println("**Old Bob takes you out**");
              finished = true;
            } else {
              System.out.println("Bob: What kind of answer is that?");
              System.out.println("**Old Bob takes you out**");
              finished = true;
            }
          } else if (bobQuestion == 2) {
            if (!answer.equals("rhododendron")) {
              System.out.println("Bob: Wrong, it's rhododendron.  But, I suppose, that's opinion too, isn't it.  Fine, you pass.\nBob: Last question: Are there ancient pyramids in Mexico?");
              bobQuestion = 3;
            } else {
              System.out.println("Bob: YES\nBob:Alright, last question: Are there ancient pyramids in Mexico?");
              bobQuestion = 3;
            }
          } else if (bobQuestion == 3) {
            if (answer.equals("y") || answer.equals("yes")) {
              System.out.println("Bob: Dang kid who doesn't know what's good for themself.\nBob: Fine, pass, see if I care.");
              chess.setExits(wind_riddle, water_riddle, spawn, victor_riddle);
            } else {
              System.out.println("Bob: HAHA, got you with that one, didn't I.\nBob: Thanks for the stones!");
              System.out.println("**Old Bob takes you out**");
              finished = true;
            }
          }
        } else {
          System.out.println("You are wrong, or there is no riddle to answer.");
        }
      }
    }
    /**
    *Performs the ritual in trinity
    */
    public void ritual() {
      if (currentRoom.equals(trinity) && currentRoom.getItems().indexOf("water_stone12") > -1 && currentRoom.getItems().indexOf("wind_stone12") > -1 && currentRoom.getItems().indexOf("ignorance_stone12") > -1) {
        System.out.println("In the middle of the triangle formed by the pedestals, a bright light forms, with seemingly no source.\nAfter a few seconds, you have to shield your eyes.  Then, with a flash, it is gone.");
        trinity.setExits(victory, victor_riddle, null, null);
        System.out.println("**A door has opened!");
        if (progress == 5) {
          progress++;
        }
      } else {
        System.out.println("The ritual is not ready!");
      }
    }
}
