package org.example;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

public class CounterOfLead implements Runnable {

    Map<Integer, Integer> sizeToFreq;

    CounterOfLead(Map<Integer, Integer> sizeToFreq) {
        this.sizeToFreq = sizeToFreq;
    }

    @Override
    public void run() {
        int i = 0;
        while (!Thread.interrupted()) {
            synchronized (sizeToFreq) {
                if (!sizeToFreq.isEmpty()) {
                    try {
                        // Поток ждет пока его не уведомят о старте
                        sizeToFreq.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    //Через компаратор находим максимум
                    Optional<Map.Entry<Integer, Integer>> mapEntry = sizeToFreq.entrySet().stream()
                            .max(Comparator.comparing(Map.Entry::getValue));
                    int count = mapEntry.get().getKey();

                    //Что бы не спамить в консоль проверяем что число-лидер обновился и только в этом случае выводим инормацию
                    if (count != i) {
                        System.out.println("Лидер среди частот: " + count);
                        i = count;
                    }
                }
            }
        }
    }
}
