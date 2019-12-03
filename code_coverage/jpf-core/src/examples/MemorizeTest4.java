class MemorizeTest4 {
    public static void main(String argv[]) {
        int a;
        double b;
        String s;
        a = 19;
        b = 23.3;
        s = "prefect";
        SampleVariable v1 = new SampleVariable(91, 89.1);
        System.out.printf("\n------ First call Add and save result -----\n");
        System.out.printf("%d + %d = %d!\n", a, (int) b, Add.add(a, b, s, v1));
        System.out.printf("\n------ Second call Add and found result -----\n");
        System.out.printf("%d + %d = %d!\n", a, (int) b, Add.add(a, b, s, v1));
        System.out.printf("\n------ Update the object without hitting the caching -----\n");
        v1.a = 9;
        System.out.printf("%d + %d = %d!\n", a, (int) b, Add.add(a, b, s, v1));
    }
}
