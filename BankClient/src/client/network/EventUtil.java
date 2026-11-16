package client.network;

/**
 * Utility class providing methods to manipulate events.
 */
public class EventUtil {
    /*
      Private constructor to prevent (pointless) instantiation of the class.
    */
    private EventUtil() {
    }

    /**
     * Utility method that extracts the type and the argument of an event from a string.
     *
     * @param str string describing the event
     * @return String[] array of size 2 containing the type and the argument of the event
     */
    public static String[] extractEventInfo(String str) {

        str = str.trim();

        if ("".equals(str))
            return new String[]{"", ""};
        else {
            int i = str.indexOf(' ');
            if (i==-1)
                return new String[]{str, ""};
            else
                return new String[]{str.substring(0,i),str.substring(i).trim()};
        }
    }
}
