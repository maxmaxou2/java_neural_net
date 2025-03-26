import java.util.ArrayList;
import java.util.Collections;

public class Random {

    public static float random(float a, float b) {
        return (((float) Math.random())*(b-a)+a);
    }

    public static int randInt(int a, int b) {
        return ((int) Math.random()*(b-a)+a);
    }

    public static int[] randomUniqueInts(int a, int b, int amount) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i=a; i<b; i++) list.add(i);
        Collections.shuffle(list);
        int[] array = new int[amount];
        for (int i=0; i < amount; i++) {
            array[i] = list.get(i);
        }
        return array;
    }
}
