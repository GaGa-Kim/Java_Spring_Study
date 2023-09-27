package chapter3.item13;

// HashTable의 복제 가능 버전
public class HashTable implements Cloneable {
    private Entry[] buckets = new Entry[50];
    private int size = 0;

    public void put(Entry entry) {
        buckets[size++] = entry;
    }

    public void printAll() {
        for (int i = 0; i < size; i++) {
            System.out.println(buckets[i].toString());
        }
    }

    static class Entry {
        final Object key;
        Object value;
        Entry next;

        public Entry(final Object key, final Object value, final Entry next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        // 재귀 호출을 이용한 깊은 복사
        /*
         * Entry deepCopy() {
         * return new Entry(key, value, next == null ? null : next.deepCopy());
         * }
         */

        // 깊은 복사를 재귀 호출 대신 반복자를 써서 순회하는 방향
        Entry deepCopy() {
            Entry result = new Entry(key, value, next);
            for (Entry p = result; p.next != null; p = p.next) {
                p.next = new Entry(p.next.key, p.next.value, p.next.next);
            }

            return result;
        }
    }

    // 복잡한 가변 상태를 참조하는 클래스용 clone 메서드
    @Override
    protected HashTable clone() {
        try {
            HashTable result = (HashTable) super.clone();
            result.buckets = new Entry[buckets.length];

            for (int i = 0; i < buckets.length; i++) {
                if (buckets[i] != null) {
                    result.buckets[i] = buckets[i].deepCopy();
                }
            }

            return result;
        } catch (CloneNotSupportedException cloneNotSupportedException) {
            throw new AssertionError();
        }
    }

    public static void main(String[] args) {
        HashTable hashTable1 = new HashTable();
        hashTable1.put(new HashTable.Entry("person", 10, null));
        hashTable1.printAll();

        HashTable hashTable2 = hashTable1.clone();
        hashTable2.printAll();
    }
}