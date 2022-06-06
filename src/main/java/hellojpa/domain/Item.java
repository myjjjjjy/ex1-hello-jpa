package hellojpa.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

// item은 굳이 상관관계 넣을 필요 없음. 아이템 입장에선 어떤 주문으로 실시간으로 팔렸는지 굳이.. 주문서로 찾지 아이템 자체로 찾이 않음.
@Entity
public class Item {
    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}
