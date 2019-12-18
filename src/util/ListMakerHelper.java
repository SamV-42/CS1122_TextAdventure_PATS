package util;

/**
*	This class is a helper object, particularly for Room.look()
*   It's based on Python generators
*
*   Date Last Modified: 12/18/19
*	@author Thomas Grifka, Sam VanderArk, Patrick Philbin, Alex Hromada
*
*	CS112, Fall 2019
*	Lab Section 2
*/

public class ListMakerHelper {
    private String finalSep;    //The final thing said at the end of the list
    private int length;         //How many items are in the list
    private int index = 0;      //internal index

    public ListMakerHelper(int length, String finalSep) {
        this.length = length;
        this.finalSep = finalSep;
    }

    public ListMakerHelper(int length) {
        this(length, ".");
    }

    /*
     * Successively returns whatever the next separator in the list is
     * for instance, calling it twice for {"Bill","Ted"}
     * would return ", " followed by "." to produce "Bill, Ted."
     * @return the next separator
     */
    public String getNextSeparator() {
        ++index;
        if(index < length - 1) {
            return ", ";
        } else if(index == length - 1) {
            if(length == 2) {
                return " and ";
            } else {
                return ", and ";
            }
        } else if(index == length) {
            return finalSep;
        } else {
            throw new java.lang.RuntimeException("Room Look Too Much");
        }
    }
}
