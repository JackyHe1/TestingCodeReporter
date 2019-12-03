### Memorization

#### Test 1

##### Call the function twice and get the value from the cache

```java
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

```

##### Result

![image-20190924010845939](/Users/lyh/Library/Application Support/typora-user-images/image-20190924010845939.png)



#### Test 2

##### Call the method with different inputs and save those results

```java
class MemorizeTest2 {
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
        System.out.printf("\n------ Call Add with different input and save result -----\n");
        b = 50.1;
        System.out.printf("%d + %d = %d!\n", a, (int) b, Add.add(a, b, s, v));
        System.out.printf("\n------ Second call Add and found result -----\n");
        System.out.printf("%d + %d = %d!\n", a, (int) b, Add.add(a, b, s, v));
        b = 23.3;
        System.out.printf("\n------ Second call Add and found result -----\n");
        System.out.printf("%d + %d = %d!\n", a, (int) b, Add.add(a, b, s, v));
    }
}

```

##### Result

![image-20190924011042853](/Users/lyh/Library/Application Support/typora-user-images/image-20190924011042853.png)



#### Test 3

##### Call the method with different **objects** and save those results

```java
class MemorizeTest3 {
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
        System.out.printf("\n------ Call Add with different input and save result -----\n");
        SampleVariable v2 = new SampleVariable(91, 100.1);
        System.out.printf("%d + %d = %d!\n", a, (int) b, Add.add(a, b, s, v2));
        System.out.printf("\n------ Second call Add and found result -----\n");
        System.out.printf("%d + %d = %d!\n", a, (int) b, Add.add(a, b, s, v1));
        System.out.printf("\n------ Second call Add and found result -----\n");
        System.out.printf("%d + %d = %d!\n", a, (int) b, Add.add(a, b, s, v2));
    }
}
```

#####Result

![image-20190924011319160](/Users/lyh/Library/Application Support/typora-user-images/image-20190924011319160.png)



#### Test 4

##### Update the object without hitting the caching

```java
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

```

#####Result

![image-20190924011620286](/Users/lyh/Library/Application Support/typora-user-images/image-20190924011620286.png)