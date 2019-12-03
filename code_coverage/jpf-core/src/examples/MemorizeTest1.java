class MemorizeTest1 {
    public static void main(String argv[]) {
        int a;
        double b;
        String s;
        a = 19;
        b = 23.3;
        s = "prefect";
        SampleVariable v = new SampleVariable(91, 89.1);
        System.out.printf("\n------ First call Add and save result -----\n");
        System.out.printf("%d + %d = %d!\n", a, (int) b, Add.add(a, b, s, v));
        System.out.printf("\n------ Second call Add and found result -----\n");
        System.out.printf("%d + %d = %d!\n", a, (int) b, Add.add(a, b, s, v));
    }
}
