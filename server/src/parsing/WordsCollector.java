package parsing;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class WordsCollector {
    public static Set<String> collectWords(InputStream stream) throws IOException {
        Set<String> words = new HashSet<>();
        try (BufferedReader bufReader = new BufferedReader(new InputStreamReader(stream))) {
            bufReader.lines().forEach(line -> words.addAll(Arrays.stream(line.toLowerCase().split("[^a-zA-Z]")).collect(Collectors.toSet())));
        }
        words.removeIf(String::isEmpty);
        return words;
    }
}
