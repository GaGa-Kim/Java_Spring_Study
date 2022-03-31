package jpabook.model.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ORDERS")
public class Order { // 주문 엔티티

    @Id @GeneratedValue
    @Column(name = "ORDER_ID")
    private Long id;

    // 외래 키가 있는 Order.member가 연관관계의 주인
    @ManyToOne // 주문과 회원은 다대일 양방향 관계
    @JoinColumn(name = "MEMBER_ID")
    private Member member; // 주문 회원 (Order -> Member로 참조하는 Order.member 필드)

    // 연관관계의 주인이 아닌 Order.orderItems에 @mappedBy 선언을 하여 연관관계의 주인인 order (OrderItem.order 필드) 지정
    @OneToMany(mappedBy = "order") // 주문과 주문상품은 일대다 양방향 관계
    private List<OrderItem> orderItems = new ArrayList<OrderItem>();

    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate; // 주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태

    // ==연관관계 메서드 (연관관계 편의 메소드)== //
    public void setMember(Member member) { // member -> order, order -> member
        // 기존 관계 제거
        if (this.member != null) {
            this.member.getOrders().remove(this);
        }
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    // Getter, Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", status=" + status +
                '}';
    }
}
