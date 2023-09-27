package chapter3.item14;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

// Comparable 구현 시의 이점
public class WordList {
    public static void main(String[] args) {
        // String이 Comparable을 구현하고 있으므로
        Set<String> s = new TreeSet<>(); // 정렬된 상태 유지
        Collections.addAll(s, args);
        System.out.println(s);
    }
}
