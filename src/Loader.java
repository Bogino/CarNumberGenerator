
import java.util.concurrent.*;

public class Loader
{
    public static void main(String[] args) throws Exception
    {

        ExecutorService es = Executors.newFixedThreadPool(4);
        ConcurrentLinkedQueue<Integer> regionCodes = new ConcurrentLinkedQueue<>();

        int region = 0;
        for (int i = 1; i <= 100; i++){
            regionCodes.add(i);
            region++;
        }

        CountDownLatch cdl = new CountDownLatch(region);

        Generator generator = new Generator(cdl, regionCodes);

        long start = System.currentTimeMillis();

        for (int i = 0; i < 4; i++){
            es.execute(new Thread(generator));
        }

        cdl.await();
        
        es.shutdown();

        System.out.printf("%d ms%n", (System.currentTimeMillis() - start));


        testThreads(1);
        testThreads(2);
        testThreads(4);
        testThreads(8);
        testThreads(16);

    }

    private static void testThreads(int count){

        ConcurrentLinkedQueue<Integer> regionCodes = new ConcurrentLinkedQueue<>();

        int region = 0;
        for (int i = 1; i <= 100; i++){
            regionCodes.add(i);
            region++;
        }

        ExecutorService es = Executors.newFixedThreadPool(count);

        CountDownLatch cdl = new CountDownLatch(region);

        Generator generator = new Generator(cdl, regionCodes);

        long start = System.currentTimeMillis();

        for (int i = 0; i < count; i++){
            es.execute(new Thread(generator));
        }
        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            es.shutdown();
            System.out.printf("Count threads: %d - %d ms%n", count, (System.currentTimeMillis() - start));
        }
    }
}
