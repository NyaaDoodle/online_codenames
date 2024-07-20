package users;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UserManager {
    private final Set<String> users = new HashSet<>();

    public synchronized void addUser(final String username) {
        users.add(username);
    }

    public synchronized void removeUser(final String username) {
        users.remove(username);
    }

    public synchronized Set<String> getUsers() {
        return Collections.unmodifiableSet(users);
    }

    public boolean doesUserExist(final String username) {
        return users.contains(username);
    }
}
