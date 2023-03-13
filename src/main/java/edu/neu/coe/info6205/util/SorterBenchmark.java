package edu.neu.coe.info6205.util;

import edu.neu.coe.info6205.sort.SortWithHelper;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import static edu.neu.coe.info6205.util.Utilities.formatWhole;

/**
 * Class to extend Benchmark_Timer for sorting an array of T values.
 * The default implementation of run in this class randomly selects a subset of the array to be sorted.
 * Each sort is preceded (optionally) by a preProcessor and succeeded (optionally) by a postProcessor.
 *
 * @param <T> the underlying type to be sorted.
 */
public class SorterBenchmark<T extends Comparable<T>> extends Benchmark_Timer<T[]> {

    /**
     * Run a benchmark on a sorting problem with N elements.
     *
     * @param N the number of elements.
     *          Not to be confused with nRuns, an instance field, which specifies the number of repetitions of the function.
     */
    public void run(int N) {
        logger.info("run: sort " + formatWhole(N) + " elements using " + this);
        sorter.init(N);
        final double time = super.runFromSupplier(() -> generateRandomArray(ts), nRuns);
        for (TimeLogger timeLogger : timeLoggers) timeLogger.log(time, N);
    }

    @Override
    public String toString() {
        return "SorterBenchmark on " + tClass + " from " + formatWhole(ts.length) + " total elements and " + formatWhole(nRuns) + " runs using sorter: " + sorter.getHelper().getDescription();
    }

    /**
     * Constructor for a SorterBenchmark where we provide the following parameters:
     *
     * @param tClass        the class of T.
     * @param preProcessor  an optional pre-processor which is applied before each sort.
     * @param sorter        the sorter.
     * @param postProcessor an optional pre-processor which is applied before each sort.
     * @param ts            the array of Ts.
     * @param nRuns         the number of runs to perform in this benchmark.
     * @param timeLoggers   the time-loggers.
     */
    public SorterBenchmark(Class<T> tClass, UnaryOperator<T[]> preProcessor, SortWithHelper<T> sorter, Consumer<T[]> postProcessor, T[] ts, int nRuns, TimeLogger[] timeLoggers) {
        super(sorter.toString(), preProcessor, sorter::mutatingSort, postProcessor);
        this.sorter = sorter;
        this.tClass = tClass;
        this.ts = ts;
        this.nRuns = nRuns;
        this.timeLoggers = timeLoggers;
    }

    /**
     * Constructor for a SorterBenchmark where we provide the following parameters:
     * For this form of the constructor, the post-processor always checks that the sort was successful.
     *
     * @param tClass       the class of T.
     * @param preProcessor an optional pre-processor which is applied before each sort.
     * @param sorter       the sorter.
     * @param ts           the array of Ts.
     * @param nRuns        the number of runs to perform in this benchmark.
     * @param timeLoggers  the time-loggers.
     */
    public SorterBenchmark(Class<T> tClass, UnaryOperator<T[]> preProcessor, SortWithHelper<T> sorter, T[] ts, int nRuns, TimeLogger[] timeLoggers) {
        this(tClass, preProcessor, sorter, sorter::postProcess, ts, nRuns, timeLoggers);
    }

    /**
     * Constructor for a SorterBenchmark where we provide the following parameters:
     * For this form of the constructor, the post-processor always checks that the sort was successful.
     * For this form of the constructor, there is no pre-processor.
     *
     * @param tClass      the class of T.
     * @param sorter      the sorter.
     * @param ts          the array of Ts.
     * @param nRuns       the number of runs to perform in this benchmark.
     * @param timeLoggers the time-loggers.
     */
    public SorterBenchmark(Class<T> tClass, SortWithHelper<T> sorter, T[] ts, int nRuns, TimeLogger[] timeLoggers) {
        this(tClass, null, sorter, ts, nRuns, timeLoggers);
    }

    private T[] generateRandomArray(T[] lookupArray) {
        return sorter.getHelper().random(tClass, (r) -> lookupArray[r.nextInt(lookupArray.length)]);
    }

    protected final SortWithHelper<T> sorter;
    protected final T[] ts;
    protected final int nRuns;
    protected final TimeLogger[] timeLoggers;
    private final static LazyLogger logger = new LazyLogger(SorterBenchmark.class);
    private final Class<T> tClass;

}

//import edu.neu.coe.info6205.sort.*;
//        import edu.neu.coe.info6205.util.*;
//
//public class SortingAlgorithmBenchmark {
//
//    public static void main(String[] args) {
//
//        int[] sizes = {10000, 20000, 40000, 80000, 160000, 256000};
//
//        Config config = ConfigTest.setupConfig();
//        int runs = config.getInt("runs");
//
//        // Define benchmark loggers for each sorting algorithm
//        Benchmark<String[]> mergeSortLogger = new Benchmark_Timer<>("MergeSort",
//                (xs) -> MergeSort.sort(xs), null);
//        Benchmark<String[]> quickSortLogger = new Benchmark_Timer<>("QuickSort",
//                (xs) -> new QuickSort<>(xs).sort(), null);
//        Benchmark<String[]> heapSortLogger = new Benchmark_Timer<>("HeapSort",
//                (xs) -> new HeapSort<>(xs).sort(), null);
//
//        // Define instrumentation loggers for each sorting algorithm
//        InstrumentationHelper<String> mergeSortInstrumentation = new InstrumentationHelper<>();
//        InstrumentationHelper<String> quickSortInstrumentation = new InstrumentationHelper<>();
//        InstrumentationHelper<String> heapSortInstrumentation = new InstrumentationHelper<>();
//        mergeSortInstrumentation.setCompares(true);
//        quickSortInstrumentation.setCompares(true);
//        heapSortInstrumentation.setCompares(true);
//        mergeSortInstrumentation.setCopies(true);
//        quickSortInstrumentation.setSwaps(true);
//        heapSortInstrumentation.setSwaps(true);
//        mergeSortInstrumentation.setHits(true);
//        quickSortInstrumentation.setHits(true);
//        heapSortInstrumentation.setHits(true);
//
//        // Run benchmarks for each sorting algorithm and size
//        for (int size : sizes) {
//            System.out.println("Sorting " + size + " elements:");
//
//            // Generate array of random strings
//            String[] array = new String[size];
//            for (int i = 0; i < size; i++) {
//                array[i] = Helper.randomString(10);
//            }
//
//            // Run instrumented sorting algorithm and get instrumentation results
//            mergeSortInstrumentation.preProcess(array);
//            MergeSort.sort(array, mergeSortInstrumentation);
//            InstrumentationLog logMergeSort = mergeSortInstrumentation.postProcess();
//
//            quickSortInstrumentation.preProcess(array);
//            new QuickSort<>(array).sort(quickSortInstrumentation);
//            InstrumentationLog logQuickSort = quickSortInstrumentation.postProcess();
//
//            heapSortInstrumentation.preProcess(array);
//            new HeapSort<>(array).sort(heapSortInstrumentation);
//            InstrumentationLog logHeapSort = heapSortInstrumentation.postProcess();
//
//            // Run non-instrumented sorting algorithm and get timing results
//            double timeMergeSort = mergeSortLogger.run(array, runs);
//            double timeQuickSort = quickSortLogger.run(array, runs);
//            double timeHeapSort = heapSortLogger.run(array, runs);
//
//            // Print instrumentation and timing results for each sorting algorithm and size
//            System.out.println("MergeSort: " + logMergeSort);
//            System.out.println("Time taken: " + timeMergeSort + " seconds");
//            System.out.println("QuickSort: " + logQuickSort);
//            System.out.println("Time taken: " + timeQuickSort + " seconds");
//            System.out.println("HeapSort: " + logHeapSort);
//            System.out.println("Time taken: " + timeHeapSort + " seconds");
//        }
//    }
//}

