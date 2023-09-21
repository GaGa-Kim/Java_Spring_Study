package chapter3.item11;

import java.util.HashMap;
import java.util.Map;

// equals를 재정의하면 hashCode로 재정의해야 함을 보여준다.
public final class PhoneNumber {
    private final short areaCode, prefix, lineNum;

    public PhoneNumber(int areaCode, int prefix, int lineNum) {
        this.areaCode = rangeCheck(areaCode, 999, "area code");
        this.prefix = rangeCheck(prefix, 999, "prefix");
        this.lineNum = rangeCheck(lineNum, 9999, "line num");
    }

    private static short rangeCheck(int val, int max, String arg) {
        if (val < 0 || val > max)
            throw new IllegalArgumentException(arg + ": " + val);
        return (short) val;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PhoneNumber))
            return false;
        PhoneNumber pn = (PhoneNumber) o;
        return pn.lineNum == lineNum && pn.prefix == prefix && pn.areaCode == areaCode;
    }

    // 최악의 hashCode 메서드
    // @Override
    // public int hashCode() {
    // return 42;
    // }

    // hashCode 없이는 제대로 동작하지 않는다. 다음 셋 중 하나를 활성화하자.

    // 전형적인 hashCode 메서드
    @Override
    public int hashCode() {
        // PhoneNumber 인스턴스의 핵심 필드 3개만을 사용해 간단한 계산만 수행
        // 기본 타입 필드라면 해당 기본 타입의 박싱 클래스인 Type에 대해서 Type.hashCode(f)를 수행
        int result = Short.hashCode(areaCode);
        result = 31 * result + Short.hashCode(prefix);
        result = 31 * result + Short.hashCode(lineNum);
        return result;
    }

    // // Object 클래스를 이용한 한 줄짜리 hashCode 메서드 - 성능이 살짝 아쉽다
    // @Override
    // public int hashCode() {
    // return Objects.hash(lineNum, prefix, areaCode);
    // }

    // // Object 클래스를 이용한 해시코드를 지연 초기화하는 hashCode 메서드 - 스레드 안정성까지 고려해야 한다.
    // private int hashCode; // 자동으로 0으로 초기화된다.
    //
    // @Override
    // public int hashCode() {
    // int result = hashCode;
    // if (result == 0) {
    // result = Short.hashCode(areaCode);
    // result = 31 * result + Short.hashCode(prefix);
    // result = 31 * result + Short.hashCode(lineNum);
    // hashCode = result;
    // }
    // return result;
    // }

    public static void main(String[] args) {
        Map<PhoneNumber, String> m = new HashMap<>();
        // hashCode를 재정의하지 않을 경우, 논리적 동치이지만 HashMap에 넣을 때와
        m.put(new PhoneNumber(707, 867, 5309), "제니");
        // 꺼내고 할 때의 PhoneNumber 인스턴스가 서로 다른 해시 코드를 반환하게 됨
        System.out.println(m.get(new PhoneNumber(707, 867, 5309)));
    }
}