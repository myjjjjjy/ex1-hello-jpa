package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

    // EntityManagerFactory는 하나만 생성해서 애플리케이션 전체 공유!
    // EntityManager는 쓰레드 간 공유 절대 안됨. 사용하고 버리기
    // JPA의 모든 데이터 변경은 트랜젝션 안에서 실행하기.

    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx =  em.getTransaction();
        tx.begin();

        try{
            // 비영속
            /* Member member = new Member();
            member.setId(100L);
            member. setName("helloJPA");

            // 영속
            em.persist(member);
            */

            // em.flush(); // 쿼리 날아가는거 바로 확인 가능, 1차 캐시 유지됨. 플래시 직접 호출
            em.createQuery("select m from Member as m", Member.class).getResultList();
            tx.commit();
        } catch (Exception e){
            tx.rollback();
        } finally {
          em.close();
        }
        emf.close();
    }
}
