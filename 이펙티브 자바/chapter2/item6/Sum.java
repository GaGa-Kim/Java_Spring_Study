package chapter2.item5;

public class Sum {
    private static long sum() {
        // 불필요한 Long 인스턴스가 약 2^31(2147483647)개 만들어지게 됨
        // long sum = 0L;으로 개선해 기본 타입 사용 필요
        Long sum = 0L;
        for (long i = 0; i <= Integer.MAX_VALUE; i++)
            sum += i;
        return sum;
    }

    public static void main(String[] args) {
        int numSets = Integer.parseInt(args[0]);
        long x = 0;

        for (int i = 0; i < numSets; i++) {
            long start = System.nanoTime();
            x += sum();
            long end = System.nanoTime();
            System.out.println((end - start) / 1_000_000. + " ms.");
        }

        // VM이 최적화하지 못하게 막는 코드
        if (x == 42)
            System.out.println();
    }
}