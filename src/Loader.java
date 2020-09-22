import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.*;

public class Loader
{

    public static void main(String[] args) throws Exception
    {

        ExecutorService es = Executors.newFixedThreadPool(8);
        ConcurrentLinkedQueue<Integer> regionCodes = new ConcurrentLinkedQueue<>();

        int region = 0;
        for (int i = 1; i <= 100; i++){
            regionCodes.add(i);
            region++;
        }

        CountDownLatch cdl = new CountDownLatch(region);

        Generator generator = new Generator(cdl, regionCodes);

        long start = System.currentTimeMillis();

        for (int i = 0; i < 8; i++){
            es.execute(new Thread(generator));
        }

        cdl.await();
        
        es.shutdown();

        System.out.println((System.currentTimeMillis() - start) + " ms");



    }

//    private static void testThreads(int count){
//
//        ExecutorService es = Executors.newFixedThreadPool(count);
//
//        CountDownLatch cdl = new CountDownLatch(regionCodes.size());
//
//        Generator generator = new Generator(cdl, regionCodes);
//
//        long start = System.currentTimeMillis();
//
//        for (int i = 0; i < count; i++){
//            es.execute(new Thread(generator));
//        }
//
//        try {
//            cdl.await();
//            es.shutdown();
//            if (count == 1){
//                System.out.println("Count threads: " + count + " - " + (System.currentTimeMillis() - start) + " ms");}
//            else {
//                System.out.println("Count threads: " + count + " - " + (System.currentTimeMillis() - start) + " ms");
//            }
//
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//            }


}
