package chapter3.item10;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class CaseInsensitiveString {
    private final String s;

    public CaseInsensitiveString(String s) {
        this.s = Objects.requireNonNull(s);
    }

    // 대칭성 위배!
    @Override
    public boolean equals(Object o) {
        // CaseInsensitiveString의 equals는 일반 String을 알고 있어 true 반환
        if (o instanceof CaseInsensitiveString)
            return s.equalsIgnoreCase(((CaseInsensitiveString) o).s);
        // String의 equals는 CaseInsensitiveString의 존재를 모르므로 false 반환
        if (o instanceof String) // 한 방향으로만 작동한다! 그러므로 CaseInsensitiveString의 equals를 String과 연동할 수 없음
            return s.equalsIgnoreCase((String) o);
        return false;
    }

    // // 수정한 equals 메서드
    // @Override public boolean equals(Object o) {
    // return o instanceof CaseInsensitiveString && ((CaseInsensitiveString)
    // o).s.equalsIgnoreCase(s);
    // }

    public static void main(String[] args) {
        CaseInsensitiveString cis = new CaseInsensitiveString("Polish");
        String s = "polish";
        System.out.println(cis.equals(s)); // true

        List<CaseInsensitiveString> list = new ArrayList<>();
        list.add(cis);

        System.out.println(list.contains(s)); // false
    }
}