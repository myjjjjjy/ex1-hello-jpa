package hellojpa.domain;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Collection;
import java.util.List;

public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            // 객체를 테이블에 맞추어 데이터 중심으로 모델링하면 협력관계를 만들 수 없음.
            // - 테이블은 외래키로 조인을 사용해서 연관된 테이블을 찾는다.
            // - 객체는 참조를 사용해서 연관된 객체를 찾는다.
            // - 테이블과 객체 사이에는 큰 간격이 있음. (객체지향스럽지 않음)

            /*
            값 타입 : 인스턴스가 달라도 그 안에 값이 같으면 같은 것으로 봐야함.
            값 타입 컬렉션도 값 타입과 동일해서 Member에 의존해서 자동으로 업데이트 됨.
            값 타입 컬렉션은 영속성 전에(Cascade)+고아객체 제거 기능을 필수로 가진다고 볼 수 있음. (지연로딩 써야함)
            값 타입은 엔티티와 다르게 식별자 개념이 없음. -> 값 변경하면 추척 어려움.
            값 타입 컬렉션에 변경사항이 발생하면, 주인 엔티티와 연관된 모든 데이터를 삭제하고, 값 타입 컬렉션에 있는 현재 값을 모두 다시 저장함.
            값 타입 컬렉션을 매핑하는 테이블은 모든 컬럼을 묶어서 기본키를 구성해야함! (not null, unique)
            -> 값 타입 컬렉션 대신 일대다 관계를 고려 -> 일대다 관계를 위한 엔티티를 만들고 여기에서 값 타입을 사용.
            영속성 전이(Cascade)+고아객체제거를 사용해서 값 타입 컬렉션처럼 사용
             */

            Member member = new Member();
            member.setUsername("member1");
            member.setHomeAddress(new Address("city1", "street", "10000"));

            member.getFavoriteFoods().add("chicken");

            //member.getAddressHistory.add(new Address("old1", "street", "10000"));

            em.persist(member);

            // 조회
            em.flush();
            em.clear();

            Member findMember = em.find(Member.class, member.getId());

            // 수정 : 인스턴스 자체를 갈아끼워야함
            Address a = findMember.getHomeAddress();
            findMember.setHomeAddress(new Address("city", a.getStreet(), a.getZipcode()));
            // 단순 String 타입일 땐 remove로 값 제거한 다음 add로 넣어줘야함.
            findMember.getFavoriteFoods().remove("chicken");
            findMember.getFavoriteFoods().add("pizza");
            // equals hashcode 제대로 넣어서 사용해야함!!!!! 아예 같은 걸 찾아서 제거한 다음 add 해주기
            findMember.getAddressHistory().remove(new AddressEntity("old1", "street", "1000"));
            findMember.getAddressHistory().add(new AddressEntity("old1", "street", "1000"));


            // JPQL : Java Persistence Query Language. 객체지향 쿼리 언어. 엔티티 중심. SQL을 추상화해서 특정 데이터 베이스에 의존하지 않음.
            // 결국 SQL로 변환되서 실행 됨.
            // query.getResultList() : 결과가 하나 이상일 때, 리스트 반환. 결과가 없으면 빈 리스트 반환! (Null point Exception 걱정 안해도됨)
            // query.getSingleList() : 결과가 딱 하나일 때, 단일 객체 반환

            List<Member> result = em.createQuery("select m from Member m order by m.age desc", Member.class)
                            .setFirstResult(1)
                                    .setMaxResults(10)
                                            .getResultList();

//            String query ="select "
//                    +"case when m.age <= 10 then '학생요금'"+
//                    "      when m.age >=60 then '경로요금'"+
//                    "      else '일반요금'"+
//                    "end "+
//                    "from Member m";

            String query2 = "select coalesce(m.username, '이름 없는 회원') " + "from Member m";
            String query3 = "select NULLIF(m.username, '관리자') " + "from Member m";

            String query4 = "select function('group_concat', m.username) From Member m";

            // coalesce : 하나씩 조회에서 Null이 아니면 반환
            // nullif : 두 값이 같으면 Null, 다르면 첫번째 값 반환

            // 상태필드 : (state field) 단순히 값을 저장하기 위한 필드 (ex. m.username) 경로탐색의 끝, 탐색 x
            // 연관필드 : (association field) 연관관계를 위한 필드
            // - 단일 값 연관 필드 : @ManyToOne, @OneToMany, 대상이 엔티티(ex. m.team)
            // - 단일 값 연관 경로 : 묵시적 내부 조인 (inner join) 발생,  탐색 o
            // - 컬렉션 값 연관 필드 : @OneToMany, @ManyToMany, 대상이 컬렉션(ex. m.orders)
            // - 컬렉션 값 연관 경로 : 묵시적 내부 조인 발생, 탐색 x , FROM 절에서 명시적 조인을 통해 별칭을 얻으면 별칭을 통해 탐색 가능,
            // 묵시적 아예 안쓰는 걸 추천, 모두 명시적 조인으로!! -> 묵시적 조인은 조인이 일어나는상황을 한 눈에 파악하기 어려움.

            // 컬렉션 자체를 지정하는 거기 때문에 size정도만 가져올 수 있음.
            String query5 = "select t.members From Team t";
            Collection result3 = em.createQuery(query5, Collection.class).getResultList();

            // List<Object[]> result2 = em.createQuery(query).getResultList();
//
//            for (Object[] objects : result2){
//                System.out.println("object = " + objects[0]);
//                System.out.println("object = " + objects[1]);
//                System.out.println("object = " + objects[2]);
//            }

            // fetch join 아주 중요!!!!!!
            // JPQL에서 성능 최적화를 위해 제공하는 기능. 연관된 엔티티나 컬렉션을 SQL 한 번에 함께 조회하는 기능.
            // JPQL - select m from Member m join fetch m.team
            // SQL  - select m.*, t.* from member m inner join team t on m.team_id =  t.id
            // 중복제거 - distinct (SQL에 distinct 추가, 애플리케이션에서 엔티티 중복 제거) -> 같은 식별자를 가진 Team 엔티티 제거
            // 페치 초인 대상에는 별칭 안주는  게 관례인데 잘~~~쓰면 좋을 때도 있음. 하이버네이트는 가능, 가급적 사용 지양하기
            // 둘 이상의 컬렉션은 페치조인 불가.
            // 컬렉션을 페치조인하면 페이징 API (setFirstResult, setMacResults)사용 불가.
            // - 일대일, 다대다 같은 단일 값 연관 필드들은 페치 조인해도 페이징 가능
            // - 하이버네이트는 경고 로그를 남기고 메모리에서 페이징 (매우위험!!! 절대 쓰지 말자!!)
            // Warning : firstResult/maxResults specified with collection fetch; applying in memory!
            // 연관된 엔티티들을 SQL 한 번으로 조회 - 성능 최절화
            // 엔티티에 직접 적용하는 글로벌 로딩 전략보다 우선함
            // - @OneToMany(fetch = FetchType.LAZY) // 글로벌 로딩 전략
            // 실무에서는 글로벌 로딩 전략은 모두 지연 로딩
            // 최적화가 꼭 필요한 곳은 페치 조인 적용 (n+1)터지는 곳만! => 성능문제 해결가능. 복잡한 view 빼고 웬만한 건 다 됨.
            // 페치조인은 객체 그래프를 유지할 떄 사용하는 효과적
            // 여러 테이블을 조인해서 엔티티가 가진 모양이 아닌 전혀 다른 결과를 내야 하면, 페치 조인보다는 일반 조인을 사용하고 필요한 데이터들만 조회해서 DTO로 반환하는 것이 효과적

            Team team1 = new Team();
            team1.setName("아스널");
            em.persist(team1);

            Member member1 = new Member();
            member1.setUsername("손흥민");
            em.persist(member1);

            String query6 = "select t from Team t join fetch t.members";
            String query7 = "select distinct t from Team t join fetch t.members where t.name = '아스널'";
            List<Member> members = em.createQuery(query6, Member.class).getResultList();

            List<Team> result4 = em.createQuery(query7, Team.class)
                    .setFirstResult(0)
                    .setMaxResults(2)
                    .getResultList();

            for (Team team : result4){
                System.out.println("team = "+team.getName()+"|members="+team.getMembers().size());
//                for (Member member1 : team.getMembers()){
//                    System.out.println("-> member =  "+member1);
//                    => 성능이 안나옴. 페이징이 들어가면 페치조인 안되니까 이 때 Team 클래스에서 @BatchSize(size=100) size는 1000 이하로 적당히 크게 설정.
//                }
            }

             tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
    }
}
