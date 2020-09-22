import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.*;

public class Loader
{
    private static CountDownLatch cdl;
    private static Generator generator;
    public static void main(String[] args) throws Exception
    {

        ExecutorService es = Executors.newFixedThreadPool(8);
        ConcurrentLinkedQueue<Integer> regionCodes = new ConcurrentLinkedQueue<>();

        int region = 0;
        for (int i = 0; i < 100; i++){
            regionCodes.add(++region);
        }

        cdl = new CountDownLatch(region);

        generator = new Generator(cdl, regionCodes);

        long start = System.currentTimeMillis();

        for (int i = 0; i < 4; i++){
            es.execute(new Thread(generator));
        }

        cdl.await();
        
        es.shutdown();

        System.out.println((System.currentTimeMillis() - start) + " ms");

        testThreads(1);
        testThreads(2);
        testThreads(4);
        testThreads(8);
        testThreads(16);
        testThreads(32);

    }

    private static void testThreads(int count){

        ExecutorService es = Executors.newFixedThreadPool(count);

        long start = System.currentTimeMillis();

        for (int i = 0; i < count; i++){
            es.execute(new Thread(generator));
        }

        try {
            cdl.await();
            es.shutdown();
            System.out.println((System.currentTimeMillis() - start) + " ms");


        } catch (InterruptedException e) {
            e.printStackTrace();
        }

            }


}
