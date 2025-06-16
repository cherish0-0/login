package hello.login.web.member;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @Data 어노테이션은 Lombok 라이브러리에서 제공하는 기능으로,
 * 다음과 같은 기능들을 자동으로 생성해준다:
 * - Getter와 Setter 메소드 생성
 * - 생성자 생성 (모든 필드를 매개변수로 받는 생성자)
 * - toString() 메소드 오버라이딩
 * - equals(), hashCode() 메소드 오버라이딩
 */
@Data
public class Member {

    private Long id; // 데이터베이스에 저장되고 관리되는 고유 id

    @NotEmpty
    private String loginId; // 사용자가 직접 입력하는 로그인 아이디

    @NotEmpty
    private String name; // 사용자 이름

    @NotEmpty
    private String password; // 사용자 비밀번호
}
