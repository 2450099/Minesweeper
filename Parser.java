import java.util.*;

/**
 * NOTE:  You should NOT have to change this class unless you are 
 *        doing a challenge task like adding more command words.
 * 
 * This parser reads user input and tries to interpret it as a 
 * command. Every time it is called it reads a line from the terminal and
 * tries to interpret the line as a two word command. It returns the command
 * as an object of class Command.
 *
 * The parser has a set of known command words. It checks user input against
 * the known commands, and if the input is not one of the known commands, it
 * returns a command object that is marked as an unknown command.
 */
class Parser 
{
    private String validCommands;

    public Parser(String validCommands) 
    {
        this.validCommands = validCommands;
    }

    /** 
     * adds a command to the given list of valid commands
     */
    public void addValidCommand(String theCommand) {
        validCommands += " " + theCommand;
    }

    /**
     * sets the valid commands
     */
    public void setValidCommands(String theCommands) {
        validCommands = theCommands;
    }

    /**
     * gets the valid commands
     */
    public String getValidCommands() {
        return validCommands;
    }

    /**
     * Print out a list of valid command words.
     */
    public void showCommands()
    {
        System.out.println(validCommands);
    }

    /**
     * Check whether a given String is a valid command word. 
     * Return true if it is, false if it isn't.
     **/
    public boolean isCommand(String aString)
    {
        return aString != null && validCommands.indexOf(aString) >= 0;
    }

    /**
     * return all valid commands
     */
    public String toString() 
    {
        return validCommands;
    }

    /** 
     * get a command from the user
     * NOTE:  You will probably not have to touch this method unless
     *        you are adding more command words.
     */
    public Command getCommand() 
    {
        String word1;
        String word2;

        System.out.print("> ");     // print prompt

        // This is how you read input from the console
        Scanner reader = new Scanner(System.in);
        String inputLine = reader.nextLine(); // holds the full input line

        // This is how you split a sentence into its parts
        Scanner tokenizer = new Scanner(inputLine);

        if(tokenizer.hasNext())
            word1 = tokenizer.next();      // get first word
        else
            word1 = null;
        if(tokenizer.hasNext())
            word2 = tokenizer.next();      // get second word
        else
            word2 = null;

        // note: we just ignore the rest of the input line.

        // Now check whether this word is known. If so, create a command
        // with it. If not, create a "null" command (for unknown command).

        if(this.isCommand(word1))
            return new Command(word1, word2);
        else
            return new Command(null, word2);
    }

}
