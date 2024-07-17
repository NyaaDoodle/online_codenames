package parsing;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class WordsCollector {
    public static Set<String> collectWords() throws IOException {
        Set<String> words = new HashSet<String>();
        try (BufferedReader bufReader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(filename))))) {
            bufReader.lines().forEach(line -> words.addAll(Arrays.stream(line.toLowerCase().split("[^a-zA-Z]")).collect(Collectors.toSet())));
        }
        words.removeIf(String::isEmpty);
        return words;
    }
}
