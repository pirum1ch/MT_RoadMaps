package org.example;

import java.util.Random;
import java.util.concurrent.Callable;

public class RouteMaps implements Callable<String> {
    public String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    @Override
    public String call() {
        String string = generateRoute("RLRFR", 100);

        /*Еслb нужно посмотреть имя потока в котором сформирован маршрут, количестdо вхождений R и саму сформированную строук
        - можно раскомментить строки:
         */

//        long countOfR = string.chars().filter(x -> x == 'R').count();
//        System.out.println(Thread.currentThread().getName() + " " + countOfR + " " + string);
        return string;
    }
}
