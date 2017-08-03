# either

## Story
Plenty of Either classes out there, but I found most of them too tedious. I needed a pragmatic Either that would keep the 
client code somewhat clean.

Totally inspired by the Either classes found in vavr.io, atlassians fugue and Playframeworks F.Either.

## Usage

```java
package me.franzgranlund;

public class Main {
    private static Either<String, Integer> compute(Integer i) {
        return i > 5 ? Either.left("I'm sorry Dave, I'm afraid I can't do that") : Either.right(i);
    }

    public static void main(String[] args) {
        Boolean success = compute(4).fold(lhv -> {
            System.out.println(lhv);
            return false;
        }, rhv -> {
            System.out.println("Computing successful");
            return true;
        });
    }
}
```
