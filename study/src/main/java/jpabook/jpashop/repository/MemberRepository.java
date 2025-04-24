package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // 컴포넌트 스캔의 대상이 되어서 자동으로 스프링 빈으로 등록된다.
@RequiredArgsConstructor
public class MemberRepository {


    // 이 어노테이션은 JPA의 엔티티 매니저를 주입해준다.
    // @PersistenceContext
    private final EntityManager em;


    public void save(Member member){
        // JPA에서 이 em.persist를 하면 영속성 컨텍스트에 해당 객체를 올린다. (여기서는 member)
        // 그때 영속성 컨텍스트는 key와 value가 있는데 여기서 key는 뭐가 되냐면 member_id 값이 키가 된다.
        // 즉 db pk와 맵핑을 한 게 key 값이 된다. 그래서 GeneratedValue(도메인에서)를 세팅을 하면 db마다 좀 다른데
        // id 값이 항상 생성 되는 것이 보장이 된다.
        // 그래서 service 단에서 memberRepo에 값을 저장 한 후, member.getId()를 했을 때 값이 항상 보장이 되는 것이다!
        em.persist(member);
    }

    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    public List<Member> findAll(){
        // 이 아래에 있는 JPQL은 SQL과 차이가 있다.
        // SQL은 테이블을 대상으로 쿼리를 적용하는데, 이건 엔티티 객체를 대상으로 쿼리를 적용한다.
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
