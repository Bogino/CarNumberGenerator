import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

public class Generator implements Runnable {


    private CountDownLatch cdl;
    private ConcurrentLinkedQueue<Integer> regions;
    private static final char letters[] = {'У', 'К', 'Е', 'Н', 'Х', 'В', 'А', 'Р', 'О', 'С', 'М', 'Т'};

    public Generator(CountDownLatch countDownLatch, ConcurrentLinkedQueue<Integer> regions){
        cdl = countDownLatch;
        this.regions = regions;
    }



    @Override
    public void run() {

//        while (!regions.isEmpty()){
//            String region = regions.poll().toString();
//            PrintWriter writer = null;
//            try {
//                writer = new PrintWriter("res/" + region + ".txt");
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            writer.write(generateNumber(region));
//            cdl.countDown();
//            writer.flush();
//            writer.close();
//        }
        while (true){
        Integer region = regions.poll();
        cdl.countDown();
        if (region == null){
            return;
        }

        Path p = Paths.get("res/",region + ".txt");
        try {
            Files.write(p,generateNumber(region.toString()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e){
            e.printStackTrace();
        }
        }

    }

    private static String generateNumber(String reg){


        StringBuilder builder = new StringBuilder();
        for(int number = 1; number < 1000; number++) {
            String carNumber = padNumber(number, 3);
            for (char firstLetter : letters) {
                for (char secondLetter : letters) {
                    for (char thirdLetter : letters) {
                        builder.append(firstLetter)
                                .append(carNumber)
                                .append(secondLetter)
                                .append(thirdLetter)
                                .append(reg).append("\n");

                    }
                }
            }
        }
        return builder.toString();
    }

    private static String padNumber(int number, int numberLength)
    {
        String numberStr = Integer.toString(number);
        StringBuilder builder = new StringBuilder();
        int padSize = numberLength - numberStr.length();
        for(int i = 0; i < padSize; i++) {
            builder.append('0');
        }
        builder.append(numberStr);
        return builder.toString();
    }
}
