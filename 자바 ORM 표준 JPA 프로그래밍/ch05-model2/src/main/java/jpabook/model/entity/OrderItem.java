package jpabook.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "ORDER_ITEM")
public class OrderItem { // 주문상품 엔티티

    @Id @GeneratedValue
    @Column(name = "ORDER_ITEM_ID")
    private Long id;

    @ManyToOne // 주문상품과 상품은 다대일 단방향 관계 (상품에서 주문상품을 참조할 일이 거의 없으므로)
    @JoinColumn(name = "ITEM_ID")
    private Item item; // 주문 상품 (OrderItem -> Item로 참조하는 OrderITem.item 필드)

    // 외래 키가 있는 OrderItem.order가 연관관계의 주인
    @ManyToOne // 주문상품과 주문은 다대일 양방향 관계
    @JoinColumn(name = "ORDER_ID")
    private Order order; // 주문

    private int orderPrice; // 주문 가격
    private int count; // 주문 수량

    // Getter, Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", buyPrice=" + orderPrice +
                ", count=" + count +
                '}';
    }
}
