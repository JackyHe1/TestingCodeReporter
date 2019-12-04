package testing;

public class C {
    int max(int x, int y) {
        int a = 1;
        int b = 2;
        for(int i = 0; i < x; i++) {
            a += b;
        }
        if (x < y) {
            return y;
        } else {
            return a;
        }
    }
}