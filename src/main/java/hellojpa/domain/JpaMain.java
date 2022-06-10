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

             */

            Address address = new Address("city", "street", "10000");
            Member member = new Member();
            member.setUsername("member1");
            member.setHomeAddress(address);
            Address newAddress = new Address("NewCity", address.getStreet(), address.getZipcode());
            member.setHomeAddress(newAddress);

            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
    }
}
