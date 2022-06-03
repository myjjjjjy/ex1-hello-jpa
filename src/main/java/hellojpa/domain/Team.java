package hellojpa.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {

    // 양방향 객체 연관관계
    // 객체 연관관계 = 2개. 회원->팀 / 팀->회원
    // 테이블 연관관계 = 1개. 팀<->회원
    // 객체의 양방향관계는 사실 서로 다른 단방향관계 2개!!
    // 테이블은 fk 하나로 투 데이블의 연관관계를 관리함. (양쪽으로 조인)
    // 둘 중에 하나로 외래키를 관리해야 하는데 그럼 둘 중에 뭘 주인으로 삼을지?? => fk가 있는 쪽을 주인으로!
    // 연관관계의 주인만이 외래키를 관리(등록, 수정), 주인이 아닌 쪽은 읽기만!!
    // 주인은 mappedBy 속성 사용 하면 안됨!!

    @Id
    @GeneratedValue
    @Column(name="TEAM_ID")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "team") // team으로 맵핑 된 관계란 걸 명시
    private List<Member> members = new ArrayList<>(); 

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

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}
