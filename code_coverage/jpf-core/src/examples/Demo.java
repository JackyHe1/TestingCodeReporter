class Demo {
    public static void main(String argv[]) {
        int a;
        double b;
        String s;
        a = 19;
        b = 23.3;
        s = "prefect";
        SampleVariable v = new SampleVariable(91, 89.1);
        System.out.printf("Hello world: %d!\n", Add.add(a, b, s, v));
        System.out.printf("Hello world: %d!\n", Add.add(a, b, s, v));
        v.a = 92;
        System.out.printf("Hello world: %d!\n", Add.add(a, b, s, v));
        v.a = 91;
        System.out.printf("Hello world: %d!\n", Add.add(a, b, s, v));
    }
}
