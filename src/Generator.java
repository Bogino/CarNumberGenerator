import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

public class Generator implements Runnable {


    private CountDownLatch cdl;
    private ConcurrentLinkedQueue<Integer> regions;

    public Generator(CountDownLatch countDownLatch, ConcurrentLinkedQueue<Integer> regions){
        cdl = countDownLatch;
        this.regions = regions;
    }



    @Override
    public void run() {

        while (!regions.isEmpty()){
            String region = regions.poll().toString();
            PrintWriter writer = null;
            try {
                writer = new PrintWriter("res/" + region + ".txt");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            writer.write(generateNumber(region));
            cdl.countDown();
            writer.flush();
            writer.close();
        }

    }

    private static String generateNumber(String reg){

        char letters[] = {'У', 'К', 'Е', 'Н', 'Х', 'В', 'А', 'Р', 'О', 'С', 'М', 'Т'};
        StringBuilder builder = new StringBuilder();
        for(int number = 1; number < 1000; number++) {
            String carNumber = padNumber(number, 3);
            for (char firstLetter : letters) {
                for (char secondLetter : letters) {
                    for (char thirdLetter : letters) {
                        builder.append(firstLetter);
                        builder.append(carNumber);
                        builder.append(secondLetter);
                        builder.append(thirdLetter);
                        builder.append(reg);
                        builder.append("\n");

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
