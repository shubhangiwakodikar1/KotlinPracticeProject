package com.smart.data;

import java.util.ArrayList;

public class GenericsExample3 {
    public static void main(String[] args) {
        //contravariant - consumer
        ArrayList<? super A1> arrayListA1 = new ArrayList<>();
        A a = (A) arrayListA1.get(0);
        A aOther = new A();
        arrayListA1.add((A1)aOther); //would be a class cast exception at runtime
        A1 a1 = new A1();
        arrayListA1.add(a1);
    }
}

class A {

}

class A1 extends A {

}


