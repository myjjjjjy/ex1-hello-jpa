package hellojpa;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Member {

    @Id
    private Long id;

    //@Column(unique = true, length = 10) // DDL 생성기능. ddl 자동생성. jpa실행 로직에는 영향 안줌.
    @Column(name="name")
    private String name;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Lob
    private String description;

    @Transient
    private int temp; // 메모리에서만 쓰겠다. 임시로 캐시데이터 넣어둘 때 사용.

    public Member(){
    }
}
