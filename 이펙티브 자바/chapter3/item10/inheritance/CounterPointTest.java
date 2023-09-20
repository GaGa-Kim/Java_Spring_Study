package chapter3.item10.inheritance;

import java.util.Set;

import chapter3.item10.Point;

// CounterPoint를 Point로 사용하는 테스트 프로그램
public class CounterPointTest {
    // 단위 원 안의 모든 점을 포함하도록 unitCircle을 초기화한다.
    private static final Set<Point> unitCircle = Set.of(
            new Point(1, 0),
            new Point(0, 1),
            new Point(-1, 0),
            new Point(0, -1));

    // 컬렉션 구현체에서 주어진 원소를 담고 있는지 확인할 때 equals 메서드 이용한다.
    public static boolean onUnitCircle(Point p) {
        return unitCircle.contains(p);
    }

    public static void main(String[] args) {
        Point p1 = new Point(1, 0);
        Point p2 = new CounterPoint(1, 0);

        // true를 출력한다.
        System.out.println(onUnitCircle(p1));

        // true를 출력해야 하지만, Point의 equals가 getClass를 사용해 작성되었다면 그렇지 않다.
        // 원래처럼 instanceof를 사용할 경우 CounterPoint는 Point를 상속했으므로 제대로 동작함
        System.out.println(onUnitCircle(p2));
    }
}