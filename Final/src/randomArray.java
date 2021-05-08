import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class randomArray {
    public static void main(String[] args) {
        Random random = new Random();
        ArrayList<Integer> arrayList = new ArrayList<Integer>();

        for (int i = 0; i < 10; i++) {
            arrayList.add(random.nextInt(10));
        }

        HashSet<Integer> tmp = new HashSet<>(arrayList);

        System.out.println(new ArrayList<Integer>(tmp));
        System.out.println(tmp);
        System.out.println(arrayList.toString());
    }
}
