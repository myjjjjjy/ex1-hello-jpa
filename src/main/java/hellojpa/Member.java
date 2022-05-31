package hellojpa;

import javax.persistence.*;
import java.util.Date;

@Entity
@SequenceGenerator(name = "member_seq_generater", sequenceName = "member_seq")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id; // Long타입 쓰는 걸 권장!

    //@Column(unique = true, length = 10) // DDL 생성기능. ddl 자동생성. jpa실행 로직에는 영향 안줌.
    @Column(name="name", nullable = false)
    private String userName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Member(){
    }
}
