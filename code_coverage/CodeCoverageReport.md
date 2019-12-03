### Code Coverage

Here I ignore all the class in the  `java` & `gov` packages.

By running `./bin/jpf +classpath=src/examples/. +listener=gov.nasa.jpf.CoverageListener Demo`, JPF will output the code coverage like the screenshot below

![image-20190924012117921](/Users/lyh/Library/Application Support/typora-user-images/image-20190924012117921.png)

##### Test Code

```java
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

class Add {
    public static int add(int a, double b, String s, SampleVariable v) {
      return a + (int) b;
    }
}

public class SampleVariable {
    public int a;
    double b;

    public SampleVariable(int a, double b) {
        this.a = a;
        this.b = b;
    }
}
```

