package est.commitdate.exception;

public class DuplicatedEmailException extends RuntimeException {

    public DuplicatedEmailException() {
        super("이미가입된 이메일 입니다.");
    }

    public DuplicatedEmailException(String message) {
        super(message);
    }

}
