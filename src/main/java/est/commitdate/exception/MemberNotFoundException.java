package est.commitdate.exception;

public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException() {
        super("가입된 회원을 찾을 수 없습니다.");
    }

    public MemberNotFoundException(String message) {
        super(message);
    }
}
