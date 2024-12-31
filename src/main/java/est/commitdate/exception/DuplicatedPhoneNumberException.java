package est.commitdate.exception;

public class DuplicatedPhoneNumberException extends RuntimeException {

    public DuplicatedPhoneNumberException() {
        super("이미 가입된 전화번호 입니다.");
    }

    public DuplicatedPhoneNumberException(String message) {
        super(message);
    }

}
