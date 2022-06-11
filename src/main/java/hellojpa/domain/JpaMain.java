package hellojpa.domain;

import com.sun.org.apache.xpath.internal.operations.Or;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
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



            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
    }
}
