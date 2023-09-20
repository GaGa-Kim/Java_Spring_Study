package chapter3.item10.inheritance;

import chapter3.item10.Color;
import chapter3.item10.Point;

// Point에 값 컴포넌트(color)를 추가
public class ColorPoint extends Point {
    private final Color color;

    public ColorPoint(int x, int y, Color color) {
        super(x, y);
        this.color = color;
    }

    // 첫 번째 equals 메서드 - 대칭성 위배!
    // 단순히 상속하는 클래스의 equals를 사용할 경우, 색상 정보는 무시한 채 비교를 수행하게 됨
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ColorPoint))
            return false;
        return super.equals(o) && ((ColorPoint) o).color == color;
    }

    // // 두 번째 equals 메서드 - 추이성 위배!
    // // 비교를 할 때 색상을 무시하도록 한다면
    // @Override public boolean equals(Object o) {
    // if (!(o instanceof Point))
    // return false;
    //
    // // o가 일반 Point면 색상을 무시하고 비교한다.
    // if (!(o instanceof ColorPoint))
    // return o.equals(this);
    //
    // // o가 ColorPoint면 색상까지 비교한다.
    // return super.equals(o) && ((ColorPoint) o).color == color;
    // }

    public static void main(String[] args) {
        // 첫 번째 equals 메서드는 대칭성을 위배한다.
        Point p = new Point(1, 2);
        ColorPoint cp = new ColorPoint(1, 2, Color.RED);
        System.out.println(p.equals(cp) + " " + cp.equals(p)); // true false

        // 두 번째 equals 메서드는 추이성을 위배한다.
        ColorPoint p1 = new ColorPoint(1, 2, Color.RED);
        Point p2 = new Point(1, 2);
        ColorPoint p3 = new ColorPoint(1, 2, Color.BLUE);
        System.out.printf("%s %s %s%n", p1.equals(p2), p2.equals(p3), p1.equals(p3)); // true true false
    }
}
