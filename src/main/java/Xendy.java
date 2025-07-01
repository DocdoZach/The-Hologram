public class Xendy {
    public static final boolean DEBUG = true;
    public static void printDebug(String string) {
        if (DEBUG) System.out.println(string);
    }

    public static void printfDebug(String format, Object... args) {
        if (DEBUG) System.out.printf((format.endsWith("\n") || format.endsWith("%n")) ? format : format + "\n", args);
    }
}
