package game.instance;

public class Pair<K, V> {
    // This was added here in case JavaFX causes dependency problems,
    // which I experienced on one of my PC's... so this is a "just in case".
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
