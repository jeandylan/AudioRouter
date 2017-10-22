package audiomix.audiooutputsource.util;

/**
 * Created by dylan on 28-Aug-17.
 */

public class StringUtil {

    public static String toTitleCase(String str) {

        if (str == null) {
            return null;
        }

        boolean space = true;
        StringBuilder builder = new StringBuilder(str);
        final int len = builder.length();

        for (int i = 0; i < len; ++i) {
            char c = builder.charAt(i);
            if (space) {
                if (!Character.isWhitespace(c)) {
                    // Convert to title case and switch out of whitespace mode.
                    builder.setCharAt(i, Character.toTitleCase(c));
                    space = false;
                }
            } else if (Character.isWhitespace(c)) {
                space = true;
            } else {
                builder.setCharAt(i, Character.toLowerCase(c));
            }
        }

        return builder.toString();
    }
    public static String toCapitalizedFirstLetter(String str){
        if(str==null) return  null;
        return (str.length()>1)?  str.substring(0,1).toUpperCase() + str.substring(1).toLowerCase():str.substring(0,1).toUpperCase();
    }
}
