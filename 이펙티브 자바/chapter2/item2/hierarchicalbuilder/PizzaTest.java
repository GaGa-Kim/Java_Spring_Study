package chapter2.item2.hierarchicalbuilder;

import static chapter2.item2.hierarchicalbuilder.Pizza.Topping.*;
import static chapter2.item2.hierarchicalbuilder.NyPizza.Size.*;

public class PizzaTest {
    public static void main(String[] args) {
        NyPizza pizza = new NyPizza.Builder(SMALL).addTopping(SAUSAGE).addTopping(ONION).build();
        Calzone calzone = new Calzone.Builder().addTopping(HAM).sauceInside().build();

        System.out.println(pizza); // [ONION, SAUSAGE]로 토핑한 뉴욕 피자
        System.out.println(calzone); // [HAM]로 토핑한 칼초네 피자 (소스는 안에)
    }
}