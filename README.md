
# Adventure Game Project

Your task is to invent and implement an adventure game. You have been given a simple framework that lets you walk through a couple of rooms. You can use this as a starting point. This project is an opportunity to show what you know about primitive data types, Strings, if statements and loops. **No arrays or advanced data structures!**

## 1 Read the Code

Reading code is an important skill that you need to practice. Your first task is to read some of the existing code and try to understand what it does. By the end of the assignment, you will need to understand much of it.

## 2 Make Small Extensions

As a little exercise to get warmed up, make some changes to the code. For example:

- change the name of a location to something different.
- change the exits – pick a room that currently is to the west of another room and put it to the north
- add a room (or two, or three, ...)
- implement a command "back" that takes the player back to the last room they were in. This will show you have the ability to add new commands to the game. (Hint:  You will need to store the last room in a variable and keep it up to date when the player leaves a room.)

These and similar exercises should get you familiar with the game.

## 3 Design Your Game

First, you should decide what the goal of your game is. It should be something along the lines of: You have to find some items and take them to a certain room (or a certain person?). Then you can get another item. If you take that to another room, you win.

For example: You are at school. You have to find out where your lab class is. To find this, you have to find the front office and ask. At the end, you need to find the final exam room. If you get there on time, and you have found your textbook somewhere along the way, and you have also been to the review class, then you win.

Or: You are lost in a dungeon. You meet a dwarf. If you find something to eat that you can give to the dwarf, then the dwarf tells you where to find a magic wand. If you use the magic wand in the big cave, the exit opens, you get out and win.

It can be anything, really. Think about the scenery you want to use (a dungeon, a city, a building, etc) and decide what your locations (rooms) are. Make it interesting, but do not make it too complicated. (I would suggest no more than 10 rooms.) I would suggest that you map out the room layout on paper before you start.

Put objects in the scenery, maybe people, monsters, etc. Decide what tasks the player has to master.

## 4 Implement the Game

Decide what commands and corresponding methods you need to make the game, then implement and test them.

## 5 Requirements

The base functionality that you have to implement is:

- The game has several locations/rooms.
- The player can walk through the locations and be able to type "back" to go to the room that they came from. (You can add other directions like up, down, etc. if you would like.)
- There are items in some rooms. You will need commands to search for items, pickup and drop items.
- The player can carry some items with them in their inventory. Every item should have a weight, value or something numeric. The player can carry items only up to a certain total weight or total value. 
- The player can win. There has to be some situation that is recognized as the end of the game where the player is informed that he/she has won.
- Add at least three more interesting non-item commands (in addition to those that were present in the code you got from me and the "back" command that you should have added previously) . Simple commands that only require a couple of lines of code are not particularly interesting.  Commands that use if statements to make decisions are interesting.
- Change the image (whenever you need to) using the ```image.setImage()``` method.

## 6 Item Implementation
The Game class has an ```inventory``` String representing the items the player is carrying.
The Room class has an ```items``` variable (manipulated with ```getItems()``` and ```setItems()```) representing the items in the room.
Dropping an item means taking it out of the Game's ```inventory``` and adding it to the Room's ```items```.
Picking up an item means taking it out of the Room's ```items``` and adding it to the Game's ```inventory```.

## 7 Item Weight Implementation
You should have a number associated with each item that gets picked up, like the item's weight, cost or points. Immediately following each item's name in the ```items``` string, put a two-digit numerical value.

For example, items could be "apple02 ball05 sword12".  This shows that the apple has a cost (or weight or points) of 2, the ball a cost of 5 and the sword a cost of 12.

Then you can get the number that corresponds to the item using a ```substring``` of the last 2 characters. This String can be converted to an integer using the ```Integer.parseInt()``` method as shown here:  

    int weight = Integer.parseInt(s);

## 8 Characters

Add characters to your game. Characters are people or animals or monsters, anything that moves, really. Characters are also in rooms like items. Unlike items, characters should be occasionally moved by the Game to different rooms.  For example, maybe a soldier is patrolling and moves between rooms occasionally.  Or a character could move to different rooms depending on what the player accomplishes.  

A character is merely another String stored in a room (just like items).  Add get/set methods for this variable (just like items have) so that the Game can manipulate the characters.

## 9 Challenge Task
Make some kind of meaningful change to the game system.  For example, 
- Extend the parser to recognize three-word commands and use three-word commands in an interesting fashion. You could, for example, have a command

_give bread dwarf_

to give some bread (which you are carrying) to the dwarf.

## 10 Header
In the main comment for your game in the header of Game.java, you should include:
- the name and a short description of your game
- the description should include at least a user level description (what does the game do?) and a brief implementation description (what are important implementation features?)
- special features of your game
