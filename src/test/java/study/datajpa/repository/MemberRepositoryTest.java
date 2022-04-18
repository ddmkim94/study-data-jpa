package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;
import study.datajpa.dto.MemberDTO;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired private MemberRepository memberRepository;
    @Autowired private TeamRepository teamRepository;
    @Autowired private EntityManager em;

    @Test
    void testMember() throws Exception {
        Member member = new Member("이민경");

        Member saveMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(saveMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll(); assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberRepository.count(); assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findMembers = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
        assertThat(findMembers.get(0).getUsername()).isEqualTo("AAA");
        assertThat(findMembers.get(0).getAge()).isEqualTo(20);
        assertThat(findMembers.size()).isEqualTo(1);
    }

    @Test
    public void findYeonSeoBy(){
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findMembers = memberRepository.findYeonSeoBy();
        assertThat(findMembers.size()).isEqualTo(2);
    }

    @Test
    public void testNamedQuery(){
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findMembers = memberRepository.findByUsername("AAA");
        assertThat(findMembers.size()).isEqualTo(2);
    }

    @Test
    public void testMethodQuery(){
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findMembers = memberRepository.findUser("AAA", 10);
        assertThat(findMembers.size()).isEqualTo(1);
    }

    @Test
    public void testValueQuery(){
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> usernameList = memberRepository.findUsernameList();
        assertThat(usernameList.size()).isEqualTo(2);
    }

    @Test
    public void findMemberAndTeam() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        List<MemberDTO> memberAndTeam = memberRepository.findMemberAndTeam();
        for (MemberDTO memberDTO : memberAndTeam) {
            System.out.println(memberDTO.getMemberId() + ", " + memberDTO.getUsername() + ", " + memberDTO.getTeamName());
        }
    }

    @Test
    public void findByNames() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findByNames = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member findByName : findByNames) {
            System.out.println(findByName.getId() + ", " + findByName.getUsername());
        }
    }

    @Test
    public void paging() {
        memberRepository.save(new Member("연서", 10));
        memberRepository.save(new Member("동민", 10));
        memberRepository.save(new Member("호위", 10));
        memberRepository.save(new Member("콧물이", 10));
        memberRepository.save(new Member("이민경", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // 외부로 반환하면 안됨!!!
        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        // map: 매핑! 내부의 내용을 다른걸로 바꾸는 것!
        Page<MemberDTO> toMap = page.map(m -> new MemberDTO(m.getId(), m.getUsername(), null)); // 얘는 외부로 반환해도됨

        List<Member> content = page.getContent(); // 조회된 데이터
        assertThat(content.size()).isEqualTo(3); // 조회된 데이터 수
        assertThat(page.getTotalElements()).isEqualTo(5); // 전체 데이터 수
        assertThat(page.getNumber()).isEqualTo(0); // 페이지 번호
        assertThat(page.getTotalPages()).isEqualTo(2); // 전체 페이지 번호
        assertThat(page.isFirst()).isTrue(); // 첫번째 페이지인가?
        assertThat(page.hasNext()).isTrue(); // 다음 페이지가 존재하는가?
    }

    @Test
    public void bulkTest() {
        memberRepository.save(new Member("연서", 10));
        memberRepository.save(new Member("동민", 10));
        memberRepository.save(new Member("호위", 10));
        memberRepository.save(new Member("콧물이", 10));
        memberRepository.save(new Member("이민경", 10));

        int updateCount = memberRepository.bulkAgePlus(10);

        assertThat(updateCount).isEqualTo(5);
        System.out.println(memberRepository.findById(1L).get().getAge());
    }

    @Test
    public void findMemberLazy() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        memberRepository.save(new Member("노휘오", 35, teamA));
        memberRepository.save(new Member("이민경", 31, teamB));

        em.flush(); // 이 시점에 insert문 실행
        em.clear();

        // List<Member> members = memberRepository.findAll();
        List<Member> members = memberRepository.findMemberFetchJoinTeam();
        // List<Member> members = memberRepository.findEntityGraphByUsername("이민경");
        for (Member member : members) {
            System.out.println(member.getTeam().getName());
        }
    }

    @Test
    public void queryHint() {
        memberRepository.save(new Member("노휘오", 31));
        em.flush();
        em.clear();

        Member findMember = memberRepository.findReadOnlyByUsername("노휘오");
        findMember.setUsername("이민경");

        em.flush(); // Update Query 실행 X!!
    }

    @Test
    public void customCall() {
        List<Member> members = memberRepository.findMemberCustom();
    }

    @Test
    public void projections() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        memberRepository.save(new Member("노휘오", 35, teamA));
        memberRepository.save(new Member("이민경", 31, teamB));

        em.flush(); // 이 시점에 insert문 실행
        em.clear();

        List<UsernameOnly> findUsername = memberRepository.findProjectionByUsername("이민경");
        assertThat(findUsername.size()).isEqualTo(1);
    }
}