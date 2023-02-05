package edu.neu.coe.info6205.util;
import java.util.Random;
import java.util.function.Supplier;
import java.util.function.Consumer;
import edu.neu.coe.info6205.sort.elementary.InsertionSort;

public class Benchmark_SortOutput {
    private static final int MIN_N = 20;
    private static Integer[] randomArray(int n) {
        Random r= new Random();
        Integer[] array = new Integer[n];
        for(int i=0;i<n;i++)
        {
            array[i]=r.nextInt(n);
        }
        return array;
    }

    private static Integer[] orderedArray(int n) {
        Integer[] array = new Integer[n];
        for(int i=0;i<n;i++)
        {
            array[i]=i+1;
        }
        return array;
    }

    private static Integer[] partiallyOrderedArray(int n) {
        Random r = new Random();
        Integer[] array = new Integer[n];
        for (int i = 0; i < n / 4; i++) {
            array[i] = i + 1;
        }
        for (int i = n / 4; i < n * 3 / 4; i++) {
            array[i] = r.nextInt(n);
        }
        for (int i = n * 3 / 4; i < n; i++) {
            array[i] = i + 1;
        }
        return array;
    }

    private static Integer[] reverseOrderedArray(int n) {
        Integer[] array = new Integer[n];
        int count =1;
        for(int i=0;i<n;i++)
        {
            array[i]=n+2-count;
            count++;
        }
        return array;
    }

    public static void main(String[] args) {

        Supplier<Integer[]> orderedSupplier = () -> orderedArray(MIN_N);
        Supplier<Integer[]> reverseSupplier= () -> reverseOrderedArray(MIN_N);
        Supplier<Integer[]> partialSupplier = () -> partiallyOrderedArray(MIN_N);
        Supplier<Integer[]> randomSupplier = () -> randomArray(MIN_N);
        InsertionSort<Integer> sorting = new InsertionSort<Integer>();

        Consumer<Integer[]> connect = (t)->{sorting.sort(t, 0,t.length);};

        Benchmark_Timer<Integer[]> bt =new Benchmark_Timer<Integer[]>("Benchmarking testing for Insertion Sort ",connect);


        System.out.println("------------------------------------------INSERTION SORT FOR ORDERED ARRAY---------------------------------------------------------------");
        for (int n = MIN_N; n <= 10240; n *= 2) {
            double orderedTime = bt.runFromSupplier(orderedSupplier, 10);
            System.out.println("Insertion sort for Ordered Array of size " + n + " takes a meantime of  " + orderedTime);
        }

        System.out.println("------------------------------------------INSERTION SORT FOR PARTIALLY ORDERED ARRAY---------------------------------------------------------------");

        for (int n = MIN_N; n <= 10240; n *= 2) {
            double partiallyTime =bt.runFromSupplier(partialSupplier, 10);
            System.out.println("Insertion sort for  Partially Ordered Array  of size " + n + " takes a meantime of  " + partiallyTime);
        }

        System.out.println("------------------------------------------INSERTION SORT FOR RANDOMLY ORDERED ARRAY---------------------------------------------------------------");

        for (int n = MIN_N; n <= 10240; n *= 2) {
            double randomTime =bt.runFromSupplier(randomSupplier, 10);
            System.out.println("Insertion sort for Randomly Ordered Array of size " + n + " takes a meantime of  " + randomTime);
        }

        System.out.println("------------------------------------------INSERTION SORT FOR REVERSELY ORDERED ARRAY---------------------------------------------------------------");
        for (int n = MIN_N; n <= 10240; n *= 2) {
            double reverseTime = bt.runFromSupplier(reverseSupplier, 10);
            System.out.println("Insertion sort for Reversely Ordered Array of size " + n + " takes a meantime of  " + reverseTime);
        }
    }
}

