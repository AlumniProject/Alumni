package Alumni.backend.infra.exception;

import javassist.bytecode.DuplicateMemberException;

public class DuplicateNicknameException extends RuntimeException {
    public DuplicateNicknameException(String message) {
        super(message);
    }
}
