package study.datajpa.dto;

import lombok.Data;

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
}
