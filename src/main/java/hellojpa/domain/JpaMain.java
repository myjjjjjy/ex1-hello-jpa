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

            Book book = new Book();
            book.setName("JPA");
            book.setAuthor("김영한");

            em.persist(book);

            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
    }
}
