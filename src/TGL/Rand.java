package TGL;

// Bailey Jia-Tao Brown
// 2023
// Better random generation

import java.util.Random;

public final class Rand {
    private static Random randObj = new Random();

    public static int nextInt(int high) {
        return (int)nextFloat(high);
    }
    public static int nextInt(int low, int high) {
        return (int)nextFloat(low, high);
    }
    public static float nextFloat() {
        return randObj.nextFloat();
    }
    public static float nextFloat(float high) {
        return randObj.nextFloat() * high;
    }
    public static float nextFloat(float low, float high) {
        return low + randObj.nextFloat() * (high - low);
    }
}
