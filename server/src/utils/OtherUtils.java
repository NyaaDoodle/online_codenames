package utils;

import java.util.Collection;
import java.util.Optional;

public class OtherUtils {
    public static <E> Optional<E> getRandomMemberFromCollection(Collection<E> collection) {
        return collection.stream().skip((long) (collection.size() * Math.random())).findFirst();
    }
}
