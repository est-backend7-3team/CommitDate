package est.commitdate.exception;

public class DuplicatedNicknameException extends RuntimeException {

    public DuplicatedNicknameException() {
        super("이미 가입된 닉네임 입니다.");
    }

    public DuplicatedNicknameException(String message) {
        super(message);
    }

}
