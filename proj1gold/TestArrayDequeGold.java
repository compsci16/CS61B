import static org.junit.Assert.*;

import org.junit.Test;

public class TestArrayDequeGold {
    @Test
    public void testADG() {
        ArrayDequeSolution<Integer> solDeq = new ArrayDequeSolution<>();
        StudentArrayDeque<Integer> studDeq = new StudentArrayDeque<>();
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            double numberBetweenZeroAndOne = StdRandom.uniform();

            if (numberBetweenZeroAndOne < 0.5) {
                solDeq.addLast(i);
                studDeq.addLast(i);
                message.append("addLast(").append(i).append(")\n");
            } else {
                solDeq.addFirst(i);
                studDeq.addFirst(i);
                message.append("addFirst(").append(i).append(")\n");
            }
        }
        for (int i = 0; i < 10; i++) {
            double numberBetweenZeroAndOne = StdRandom.uniform();
            Integer a, b;

            if (numberBetweenZeroAndOne < 0.5) {
                a = solDeq.removeLast();
                b = studDeq.removeLast();
                message.append("removeLast()\n");
            } else {
                a = solDeq.removeFirst();
                b = studDeq.removeFirst();
                message.append("removeFirst()\n");
            }
            assertEquals(message.toString(), a, b);
        }
    }
}
