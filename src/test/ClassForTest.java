package test;

import application.Calculator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

public class ClassForTest {
    public static void main(String[] args) {


        start(Calculator.class);
    }

    public static void start(Class testClass) {
        List<Method> methods = new ArrayList<>(Arrays.asList(testClass.getDeclaredMethods()));
        Map<Method, Integer> mapMethods = new LinkedHashMap<>();
        int quantityBeforeSuite = 0;
        int quantityAfterSuite = 0;

        for (Method m : methods) {
            if (m.isAnnotationPresent(BeforeSuite.class)) {
                quantityBeforeSuite++;
                if (quantityAfterSuite > 1) {
                    throw new RuntimeException("Существует более одной аннотации @AfterSuite");
                }
                mapMethods.put(m, m.getAnnotation(BeforeSuite.class).priority());
            }

            if (m.isAnnotationPresent(AfterSuite.class)) {
                quantityAfterSuite++;
                if (quantityBeforeSuite > 1) {
                    throw new RuntimeException("Существует более одной аннотации @BeforeSuite");
                }
                mapMethods.put(m, m.getAnnotation(AfterSuite.class).priority());
            }
            if (m.isAnnotationPresent(Test.class)) {
                mapMethods.put(m, m.getAnnotation(Test.class).priority());
            }

        }

        mapMethods = sortByValueReverse(mapMethods);

        // массив для тестирования, элемент parameters[][0]  - ожидаемый результат
        //элементы [][1] и [][2] подаваемые параметры
        int[][] parameters = new int[][]{
                {11, 2, 3},
                {5, 2, 3},
                {6, 2, 3},
                {0, 2, 3},
                {-1, 2, 3},
                {8, 2, 3},
                {8, 2, 3}
        };

        if (parameters.length < mapMethods.size()) {
            throw new RuntimeException("Для тестов заготовлено слишком мало параметров");
        } else if (parameters.length > mapMethods.size()) {
            throw new RuntimeException("Для тестов заготовлено слишком много параметров");
        }

        int rowParam = 0;


        for (Map.Entry<Method, Integer> entry : mapMethods.entrySet()) {
            try {

                int expRes = parameters[rowParam][0];
                int a1 = parameters[rowParam][1];
                int a2 = parameters[rowParam][2];
                int res = (int) entry.getKey().invoke(testClass.newInstance(), a1, a2);

                System.out.println("Тест метода: " + entry.getKey().getName() + " -- приоритет теста: " + entry.getValue());
                System.out.println("Входные параметры: " + a1 + " и " + a2 + ". /Результат: " + res
                        + " /Ожидаемый результат: " + expRes + ". Проверка пройдена? - " + (res == expRes));
                System.out.println("________________________________________________________________________________________");

                rowParam++;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }

    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueReverse(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K, V>> unSortedMap = map.entrySet().stream();
        unSortedMap.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

}
