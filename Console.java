public class Console {

    public static String lastLine = "";
    public static long startTime;
    public static boolean running = false;
    public static int total_steps = 0, current = 0;

    public static void startProgressBar(int lines, int total_steps, int current) {
        Console.current = current;
        Console.total_steps = total_steps;
        System.out.println("[Progress] 0.0% - ...s remaining");
        for (int i = 1; i < lines; i++) {
            System.out.println();
        }
        startTime = System.currentTimeMillis();
        running = true;
    }

    public static void startProgressBar(int lines, int total_steps) {
        Console.total_steps = total_steps;
        Console.current = 0;
        System.out.println("[Progress] 0.0% - ...s remaining");
        for (int i = 1; i < lines; i++) {
            System.out.println();
        }
        startTime = System.currentTimeMillis();
        running = true;
    }

    public static void startProgressBar(int lines) {
        Console.total_steps = 0;
        Console.current = 0;
        System.out.println("[Progress] 0.0% - ...s remaining");
        for (int i = 1; i < lines; i++) {
            System.out.println();
        }
        startTime = System.currentTimeMillis();
        running = true;
    }

    public static void progress(int lines) {
        if (!Console.running)
            return;
        Console.current += 1;
        float percentage = 100.0f*current/total_steps;
        int remaining = (int)((100-percentage)/(percentage+0.0000001)*((System.currentTimeMillis()-startTime)/1000f));
        overWrite("[Progress] "+formatFloat(percentage, 1)+"% - "+remaining+"s remaining                     ", lines);
    }

    public static void progress(float percentage, int lines) {
        if (!running)
            return;
        int remaining = (int)((100-percentage)/(percentage+0.0000001)*((System.currentTimeMillis()-startTime)/1000f));
        overWrite("[Progress] "+formatFloat(percentage, 1)+"% - "+remaining+"s remaining                     ", lines);
    }

    public static void end() {
        Console.current = 0;
        Console.total_steps = 0;
        running = false;
    }

    public static String formatFloat(float number, int decNumbers) {
        return ""+((int)(number*Math.pow(10, decNumbers)))/Math.pow(10, decNumbers);
    }

    public static void overWrite(String line, int lines) {
        System.out.print(String.format("\033[%dA",lines));
        lastLine = line;
        System.out.println(line);
    }

    public static void clearLast() {
        clearLine(lastLine);
    }

    public static void clearLine(int length) {
        String clear = "\b";
        for (int i = 0; i < length; i++) {
            clear += "\b";
        }
        System.out.print(clear);
    }

    public static void clearLine(String line) {
        String clear = "";
        for (int i = 0; i < line.length(); i++) {
            clear += "\b";
        }
        System.out.println(clear);
    }
}
