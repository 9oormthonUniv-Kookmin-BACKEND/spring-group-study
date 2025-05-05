package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("Yun");

        //when
        Long savedId = memberService.join(member);

        //then
        // 이게 가능한 이유는 각 jpa에서 같은 트랜잭션 내에서 같은 Entity
        // 즉 Id값(PK 값)이 똑같으면 같은 영속성 Context에서 똑같은 애로 관리된다.
        assertEquals(member, memberRepository.findOne(savedId));

    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception{
        // given
        Member member1 = new Member();
        member1.setName("Yun");

        Member member2 = new Member();
        member2.setName("Yun");

        // when
        memberService.join(member1);
        memberService.join(member2); // 예외가 발생해야 한다.

        // then
        fail("예외가 발생해야 한다.");
    }

}