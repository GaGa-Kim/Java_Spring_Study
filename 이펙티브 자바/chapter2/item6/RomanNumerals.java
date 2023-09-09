package chapter2.item6;

import java.util.regex.Pattern;

public class RomanNumerals {
    static boolean isRomanNumeralSlow(String s) {
        // 내부에서 만드는 정규표현식용 Pattern 인스턴스(한 번 쓰고 버려져서 곧바로 가비지 컬렉션 대상이 됨)
        return s.matches("^(?=.)M*(C[MD]|D?C{0,3})" + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");
    }

    // 정규표현식용 Pattern 인스턴스를 클래스 초기화 과정에서 직접 생성해 캐싱
    private static final Pattern ROMAN = Pattern
            .compile("^(?=.)M*(C[MD]|D?C{0,3})" + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");

    // Pattern 인스턴스 재활용
    static boolean isRomanNumeralFast(String s) {
        return ROMAN.matcher(s).matches();
    }

    public static void main(String[] args) {
        int numSets = Integer.parseInt(args[0]);
        int numReps = Integer.parseInt(args[1]);
        boolean b = false;

        for (int i = 0; i < numSets; i++) {
            long start = System.nanoTime();
            for (int j = 0; j < numReps; j++) {
                // 성능 차이를 확인하려면 xxxSlow 메서드를 xxxFast 메서드로 바꿔 실행해보자.
                b ^= isRomanNumeralSlow("MCMLXXVI");
            }
            long end = System.nanoTime();
            System.out.println(((end - start) / (1_000. * numReps)) + " μs.");
        }

        // VM이 최적화하지 못하게 막는 코드
        if (!b)
            System.out.println();
    }
}