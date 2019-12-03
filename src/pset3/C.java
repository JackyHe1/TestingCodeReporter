package pset3;

public class C {
    int max(int x, int y) {
        int a = 1;
        int b = 2;
//        if(x > 2) {
//            a += b;
//        }
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