package tko.exception;

public class UserAlreadyExists extends RuntimeException {
    public UserAlreadyExists(String message) {
        super("User " + message + " already exists");
    }
}
