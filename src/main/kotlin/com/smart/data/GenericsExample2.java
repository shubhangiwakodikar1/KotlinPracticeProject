package com.smart.data;

import java.util.ArrayList;
import java.util.Collection;

public class GenericsExample2 {
    void copyAll(Collection1<Object> to, Collection1<String> from) {
        to.addAll1(from);
        // !!! Would not compile with the naive declaration of addAll:
        // Collection<String> is not a subtype of Collection<Object>
    }

    public static void main(String[] args) {
        //contravariance - write safe - consumer - ? super String - 'in' variance annotation in Kotlin
        ArrayList<? super String> stringArrayList = new ArrayList<>();
        stringArrayList.add("2");
        stringArrayList.add("1");
        stringArrayList.add("true");
        Object zeroth = stringArrayList.get(0);
//        String zeroth = stringArrayList.get(0); //doesnt compile

        //covariance - read safe - producer - ? extends Object - 'out' variance annotation in Kotlin
        ArrayList<String> normalStringArrayList = new ArrayList<>();
        normalStringArrayList.add("2");
        normalStringArrayList.add("1");
        normalStringArrayList.add("true");
        Collection1<String> objectArrayList = (Collection1<String>) new ArrayList<String>();
        objectArrayList.add("3");
//        objectArrayList.addAll1(stringArrayList); //doesnt work
        ArrayList<? super String> superStringArrayList = new ArrayList<>();
        superStringArrayList.add("2");
        Collection2<Object> objectCollection2 = new Collection2<>();
        Collection<? extends Object> objectCollection = objectCollection2.createProducer2(normalStringArrayList);
        for (Object objectReturned : objectCollection) {
            System.out.println(objectReturned);
        }
//        objectCollection.add("string") //doesnt compile
    }
}

class Collection2<E> {
    Collection<? extends E> createProducer2(Collection<? extends E> collectionToUse) {
        return collectionToUse;
    }
}

interface Collection1<E> extends Collection<E> {
    boolean addAll1(Collection<? extends E> items);
}


