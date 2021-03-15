package domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author : choi-ys
 * @date : 2021/03/15 2:27 오후
 * @Content : 회원 Entity
 */
@Entity @Table(name = "member_tb")
@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
public class MemberEntity {

    @Id
    @Column(name = "member_no")
    private Long memberNo;

    @Column(name = "member_name", length = 25, nullable = false)
    private String memberName;
}