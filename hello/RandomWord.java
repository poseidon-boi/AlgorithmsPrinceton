import java.util.Scanner;

public class RandomWord {
    public static void main(String[] args) {

        int index = 0;
        String champion = "";

        Scanner scan = new Scanner(System.in);

        while (scan.hasNext()) {
            String word = scan.next();
            double rand = Math.random();
            boolean accept = rand <= (1.0 / (index + 1.0));
            if (accept) {
                champion = word;
            }
            index++;
        }
        System.out.println(champion);

    }
}