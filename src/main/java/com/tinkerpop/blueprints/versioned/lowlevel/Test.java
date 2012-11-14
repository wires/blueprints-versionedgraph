package com.tinkerpop.blueprints.versioned.lowlevel;

class A{}
class B extends A {}

public class Test
{
    public static int foo(A a, A b) {
        return 0;
    }

    public static int foo(B a, B b) {
        return 1;
    }

    public static int foo(A a, B b) {
        return 2;
    }

    public static int foo(B a, A b) {
        return 3;
    }

    public static void main(String args[])
    {
        final B a = new B();
        final A b = new B();

        System.out.println(foo(a,b));
    }
}
