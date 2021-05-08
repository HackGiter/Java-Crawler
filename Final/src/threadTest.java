public class threadTest implements Runnable {

    private int i;

    @Override
    public void run() {
        while (i < 100) {
            // 线程锁, 共享i
            synchronized (this) {
                System.out.println(Thread.currentThread().getName() + " " + i);
                i++;
            }
        }
    }


    public static void main(String[] args) {
        for (int a = 0; a < 100; a++) {
            System.out.println(Thread.currentThread().getName() + " " + a);
            if (a == 20) {
                threadTest s1 = new threadTest();
                new Thread(s1, "Thread 1").start();
                new Thread(s1, "Thread 2").start();
            }
        }
    }
}
