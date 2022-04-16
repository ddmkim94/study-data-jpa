package study.datajpa.dto;

import lombok.Data;
import study.datajpa.entity.Member;

@Data
public class MemberDTO {

    private Long memberId;
    private String username;
    private String teamName;

    public MemberDTO(Long memberId, String username, String teamName) {
        this.memberId = memberId;
        this.username = username;
        this.teamName = teamName;
    }

    /**
     * DTO는 엔티티를 봐도 ㄱㅊ!
     * -> 파라미터로 엔티티를 받아도 상관없다. 단, 필드로는 절대 받으면 안됨!!
     */
    public MemberDTO(Member member) {
        this.memberId = member.getId();
        this.username = member.getUsername();
    }
}
