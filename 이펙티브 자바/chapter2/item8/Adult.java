package chapter2.item8;

// tyr-with-resources 블록을 활용
public class Adult {
    public static void main(String[] args) {
        // java.lang.AutoCloseable 인터페이스의 close 메소드를 호출
        // Cleaner를 통한 자동 청소를 필요하지 않음
        try (Room myRoom = new Room(7)) {
            System.out.println("안녕~"); // 안녕~ 방 청소
        }
    }
}
