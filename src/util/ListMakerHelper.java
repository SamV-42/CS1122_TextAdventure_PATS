package util;

public class ListMakerHelper {
    private String finalSep;
    private int length;
    private int index = 0;

    public ListMakerHelper(int length, String finalSep) {
        this.length = length;
        this.finalSep = finalSep;
    }

    public ListMakerHelper(int length) {
        this(length, ".");
    }

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
            return ".";
        } else {
            throw new java.lang.RuntimeException("Room Look Too Much");
        }
    }
}
