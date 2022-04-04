package jpabook.model.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Category { // 카테고리 엔티티

    @Id @GeneratedValue
    @Column(name = "CATEGORY_ID")
    private Long id;

    private String name;

    @ManyToMany // Category와 Item은 다대다 관계, 연관관계의 주인
    // @ManyToMany와 @JoinTable을 사용해 CATEGORY_ITEM 연결 테이블을 바로 매핑
    /* 다대다 관계는 연결 테이블을 JPA가 알아서 처리해주므로 편리하지만
       연결 테이블에 필드가 추가되면 더는 사용할 수 없으므로
       CategoryItem이라는 연결 테이블을 만들어서 일대다, 다대일 관계로 매핑하는 것을 권장 */
    @JoinTable(name = "CATEGORY_ITEM",
            joinColumns = @JoinColumn(name = "CATEGORY_ID"),
            inverseJoinColumns = @JoinColumn(name = "ITEM_ID"))
    private List<Item> items = new ArrayList<Item>();

    // 카테고리의 계층 구조를 위한 필드들
    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<Category>();

    // ==연관관계 메서드== //
    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }

    public void addItem(Item item) {
        items.add(item);
    }

    // Getter, Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public List<Category> getChild() {
        return child;
    }

    public void setChild(List<Category> child) {
        this.child = child;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
