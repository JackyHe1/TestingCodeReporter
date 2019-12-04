public class Demo {
    static int max(int x, int y) {
        int a = 1;
        int b = 2;

        if (x < y) {
            a += b;
        }

        if (x < y) {
            return y;
        } else {
            return a;
        }
    }

    public static void main(String argv[]) {
        max(1, 3);
    }
}