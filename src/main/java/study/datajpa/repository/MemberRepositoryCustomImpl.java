package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
/**
 * 사용자 정의 인터페이스 구현 클래스의 이름
 * -> 레포지토리 인터페이스 이름 + Impl (관례임.. 그냥 따라라..)
  */
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom{

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
}
