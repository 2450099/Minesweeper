/**
 * Room - a room in an adventure game.
 * 
 *  IMPORTANT:  Update this with information about your game
 *  
 * "Room" represents one location in the scenery of the game.  It is 
 * connected to four other rooms via exits.  The exits are labelled
 * north, east, south, west.  For each direction, the room stores a reference
 * to the neighbouring room, or null if there is no exit in that direction.
 */

class Room 
{
    // a description of the room
    private String description;
    // the items that are in the room
    private String items;
    // the available exits from the room (null if not available)
    private Room north, south, east, west;

    /**
     * Create a room described "description". Initially, it has no exits.
     * "description" is something like "a kitchen" or "an open court yard".
     */
    public Room(String description) 
    {
        this.description = description;
        items = "";
        north = null;
        south = null;
        east = null;
        west = null;
    }

    /**
     * Define the exits of this room.  Every direction either leads to
     * another room or is null (no exit there).
     */
    public void setExits(Room theNorth, Room theEast, Room theSouth, Room theWest) 
    {
        north = theNorth;
        east = theEast;
        south = theSouth;
        west = theWest;
    }

    /**
     * Return the description of the room 
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Change the description of the room 
     */
    public void setDescription(String newDescription)
    {
        description = newDescription;
    }

    /**
     * Return the items in the room 
     */
    public String getItems()
    {
        return items;
    }

    /**
     * Change the items in the room 
     */
    public void setItems(String newItems)
    {
        items = newItems;
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west ".
     */
    private String exitString()
    {
        String returnString = "Exits:";
        if (north != null)
            returnString += " north";
        if (east != null)
            returnString += " east";
        if (south != null)
            returnString += " south";
        if (west != null)
            returnString += " west";
        return returnString;
    }

    /**
     * Return a long description of this room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     */
    public String toString()
    {
        return "You are in " + description + ".\n" + exitString();
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     */
    public Room nextRoom(String direction) 
    {
        if (direction.equals("north"))
            return north;
        else if (direction.equals("east"))
            return east;
        else if (direction.equals("south"))
            return south;
        else if (direction.equals("west"))
            return west;
        else
            return null;
    }
}
