package study.datajpa.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

/**
 * 공통 매핑 정보가 필요할 때 사용
 * 부모 클래스에 선언하고 속성만 상속 받아서 사용할 수 있다!
  */
@MappedSuperclass
@Getter
public class JpaBaseEntity {

    // 해당 컬럼은 값이 변경되도 DB에 반영되지 않음
    @Column(updatable = false)
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    // persist 전에 실행되는 메서드
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        updatedDate = now;
    }

    // update 전에 실행되는 메서드
    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }
}
