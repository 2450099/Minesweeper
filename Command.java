/**
 * IMPORTANT:  You should NOT have to change this class unless you are doing
 *             a challenge task like adding a third word.
 * This class holds information about a command that was issued by the user.
 * A command currently consists of two strings: a command word and a second
 * word (for example, if the command was "take map", then the two strings
 * obviously are "take" and "map").
 * 
 * The way this is used is: Commands are already checked for being valid
 * command words. If the user entered an invalid command (a word that is not
 * known) then the command word is null.
 *
 * If the command had only one word, then the second word is null.
 *
 * The second word is not checked at the moment. It can be anything.
 */

class Command
{
    private String commandWord;
    private String secondWord;
    private String thirdWord;

    /**
     * Create a command object. First and second word must be supplied, but
     * either one (or both) can be null. The command word should be null to
     * indicate that this was a command that is not recognised by this game.
     */
    public Command(String firstWord, String secondWord, String thirdWord)
    {
        this.commandWord = firstWord;
        this.secondWord = secondWord;
        this.thirdWord = thirdWord;
    }

    /**
     * Return the command word (the first word) of this command. If the
     * command was not understood, the result is null.
     */
    public String getCommandWord()
    {
        return this.commandWord;
    }

    /**
     * Return the second word of this command. Returns null if there was no
     * second word.
     */
    public String getSecondWord()
    {
        return this.secondWord;
    }

    public String getThirdWord() {
      return this.thirdWord;
    }
    /**
     * Return true if this command was not understood.
     */
    public boolean isUnknown()
    {
        return (this.commandWord == null);
    }

    /**
     * Return true if the command has a second word.
     */
    public boolean hasSecondWord()
    {
        return (this.secondWord != null);
    }

    public boolean hasThirdWord() {
      return (this.thirdWord != null);
    }
}

