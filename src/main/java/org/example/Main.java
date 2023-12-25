package org.example;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    private static int threadCount = 1000;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //Создаем пул потоков для генерации маршрутов
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        // Создаем отдельный поток для подсчета лидера среди вхождений и стартуем поток
        Thread counterThread = new Thread(new CounterOfLead(sizeToFreq));
        counterThread.start();

        //В цикле по числу потоков генерируем маршруты
        for (int i = 0; i < threadCount; i++) {
            String str = threadPool.submit(new RouteMaps()).get();
            long a = str.chars().filter(x -> x == 'R').count();

            synchronized (sizeToFreq) {
                addToMap((int) a);
                // после каждого добавления в мапу значений - уведомляем поток counterThread что он может стартовать работу
                sizeToFreq.notify();
            }
        }
        counterThread.interrupt();
        print();
        threadPool.shutdown();
    }

    /*
        Метод добавляет значения в мапу
        Если в мапе нет аналогичного значния - записываем 1
     */
    private static void addToMap(int count) {
        if (!sizeToFreq.containsKey(count)) {
            sizeToFreq.put(count, 1);
        } else {
            int a = sizeToFreq.get(count);
            a++;
            sizeToFreq.put(count, a);
        }
    }


    /*
    Выводить инсформацию по данным в мапе
    Для получения максимального значения в мапе - перевожу мапу в лист и сортирую по значению
     */
    public static void print() {
        int entryTimes = 0;
        ArrayList<Map.Entry<Integer, Integer>> list = new ArrayList<>(sizeToFreq.entrySet());
        Collections.sort(list, Map.Entry.comparingByValue());

        int maxValue = list.get(list.size() - 1).getValue();
        int maxTimes = list.get(list.size() - 1).getKey();

        System.out.printf("\nСамое частое количество повторений %s (встретилось %s раз)\n", maxTimes, maxValue);
        System.out.println("Другие результаты:");

        for (Map.Entry<Integer, Integer> pair : sizeToFreq.entrySet()) {
            if (pair.getKey() > entryTimes) {
                entryTimes = pair.getKey();
            }
            System.out.println("-" + pair.getKey() + " (" + pair.getValue() + " раз)");
        }

    }

}