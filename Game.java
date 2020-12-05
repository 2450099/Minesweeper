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
    private final String INITIAL_COMMANDS = "go quit help";
    // This is the current room that that player is in
    private Room currentRoom;
    // This is the list of items that the person is carrying
    private String inventory;
    // tells if the game is over or not
    private boolean finished;
    // These are references to the rooms that are available.  
    // The actual rooms are created in createRooms().
    private Room outside, lab, store, gym, office;  

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
        inventory = "";
        finished = false;
        image = new Image("images/lfhs.jpg");
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        // create the rooms
        outside = new Room("outside the building");
        lab = new Room("the lab");
        store = new Room("the store");
        gym = new Room("the gym");
        office = new Room("the main office");

        // initialise room exits
        outside.setExits(null, lab, gym, store);
        lab.setExits(null, null, null, outside);
        store.setExits(null, outside, null, null);
        gym.setExits(outside, office, null, null);
        office.setExits(null, null, null, gym);

        currentRoom = outside;  // start game outside
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
        System.out.println("Welcome to Adventure!");
        System.out.println("Adventure is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
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
        } else if (commandWord.equals("go"))
        {
            goRoom(command);
        } else if (commandWord.equals("quit")) {
            finished = true;  // signal that we want to quit
        } else {
            System.out.println("Command not implemented yet.");
        }

    }

    /**
     * Print out some help information.
     * Here we print a cryptic message and a list of the 
     * command words.  Maybe you can improve on this.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at Lake Forest High School.");
        System.out.println();
        System.out.println("Your command words are:");
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
            currentRoom = nextRoom;
            System.out.println(currentRoom);
        }
    }
}
