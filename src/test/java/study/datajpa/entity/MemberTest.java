package study.datajpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;
import java.util.List;

@SpringBootTest
public class MemberTest {

    @Autowired
    EntityManager em;

    @Test
    @Transactional
    @Rollback(value = false)
    void testMember() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        // flush: 영속성 컨텍스트의 내용을 데이터베이스에 동기화 -> insert SQL 실행!
        em.flush();
        // clear: 영속성 컨텍스트 내용 초기화
        em.clear();

        System.out.println("==== SELECT QUERY ====");
        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
        System.out.println("==== SELECT QUERY ====");
        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("=> member.team = " + member.getTeam()); // Team 프록시 객체 초기화 (LAZY)
        }
    }
}
