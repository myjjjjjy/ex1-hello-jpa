package hellojpa.domain;

import jdk.internal.jimage.ImageStrings;
import org.hibernate.engine.internal.Cascade;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Member extends BaseEntity{

    /*
    값타입은 단순하고 안전하게 다룰 수 있어야함.
    임베디드 타입 같은 값 타입을 여러 엔티티에서 공유하면 위험!-> side effect 발생
    만약 같은 걸 공유시키고 싶으면 엔티티 써야함.
    그래서 값을 복사해서 사용해야 함.
    객체 타입은 참조값을 직접 대입하는 것을 막을 방법이 없음. 객체의 공유 참조는 피할 수 없음.
    값 타입은 불변객체(immutable object)로 설계햐아함.
    - 불변객체 : 생성ㄹ 시점 이후 절대 값을 변경할 수 없는 객체.
    셍성자로만 값을 설정하고 setter 안만들면 됨! * Integer, String은 자바가 제공하는 대표적인 불변객체.

     */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // 기본이 auto. 생략된경우도 auto.
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    @Embedded
    private Address homeAddress;

    @Embedded
    private Address address;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @ElementCollection
    @CollectionTable(name = "FAVORITE_FOOD", joinColumns = @JoinColumn(name = "MEMBER_ID")) // 테이블 만들어서 매핑 후 조인
    @Column(name = "FOOD_NAME") // 값이 하나고 SET을 내가 정의한 게 아니니까 예외적으로 이것만 명시해서 바꿔주기
    private Set<String> favoriteFoods = new HashSet<>();

//    @ElementCollection
//    @CollectionTable(name = "ADDRESS", joinColumns = @JoinColumn(name = "MEMBER_ID"))
//    private List<Address> addressHistory = new ArrayList<>(); 이게 아니라
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) // 이 방법으로 엔티티 만든 후 해주기
    @JoinColumn(name = "MEMBER_ID")
    private List<AddressEntity> addressHistory = new ArrayList<>();

    // 기간 Period
    @Embedded
    private Period workPeriod;

    public Set<String> getFavoriteFoods() {
        return favoriteFoods;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public void setFavoriteFoods(Set<String> favoriteFoods) {
        this.favoriteFoods = favoriteFoods;
    }

    public List<AddressEntity> getAddressHistory() {
        return addressHistory;
    }

    public void setAddressHistory(List<AddressEntity> addressHistory) {
        this.addressHistory = addressHistory;
    }

    public Period getWorkPeriod() {
        return workPeriod;
    }

    public void setWorkPeriod(Period workPeriod) {
        this.workPeriod = workPeriod;
    }
}

