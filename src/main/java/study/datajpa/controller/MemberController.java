package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDTO;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long memberId) {
        Member findMember = memberRepository.findById(memberId).get();
        return findMember.getUsername();
    }

    /**
     * 도메인 클래스 컨버터는 id값으로 엔티티로 바꿔주는 컨버터가 실행
     * @PathVariable에 넘어가는 값은 반드시 id값이여야함.
      */

    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    // 의존성 주입이 이루어진 후 초기화를 수행하는 메서드
    @PostConstruct
    public void createMember () {
        memberRepository.save(new Member("memberA", 10));
        memberRepository.save(new Member("memberB", 20));
    }

    @GetMapping("/page")
    public Page<MemberDTO> paging() {

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
        return page.map(m -> new MemberDTO(m.getId(), m.getUsername(), null)); // 얘는 외부로 반환해도됨
    }
}
