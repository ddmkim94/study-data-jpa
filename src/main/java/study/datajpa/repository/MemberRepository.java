package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;
import study.datajpa.dto.MemberDTO;

import javax.persistence.Entity;
import javax.persistence.QueryHint;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findYeonSeoBy();

    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    // 단일 값 조회
    @Query("select m.username from Member m")
    List<String> findUsernameList();

    // DTO 조회
    @Query("select new study.datajpa.dto.MemberDTO(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDTO> findMemberAndTeam();

    // 컬렉션 파라미터 바인딩
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    Page<Member> findByAge(int age, Pageable pageable);

    /**
     * SELECT문이 아니라는 걸 나타냄
     * clearAutomatically => 벌크 연산이 끝나고 영속성 컨텍스트와 데이터베이스의 데이터 차이를 없애기 위해
     * 영속성 컨텍스트를 초기화 시킴
     */
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age = :age")
    int bulkAgePlus(@Param("age") int age);

    // fetch join을 이용해서 데이터 한 번에 가져오기 (N+1 문제 해결!)
    @Query("select m from Member m join fetch m.team")
    List<Member> findMemberFetchJoinTeam();

    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(String username);

    // 읽기 전용
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);
}
