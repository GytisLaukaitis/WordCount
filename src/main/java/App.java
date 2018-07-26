import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Read all files from resources/input directory
 * Count occurrences of each word
 * Write results into 4 files, according to the first letter of the word, save results to resources/output dir
 */
public class App {

    public static void main(String[] args) throws Exception {

            App app = new App();
            app.run();

    }

    public void run() throws Exception {

        /**
         * Group Strings by the first letter
         */
        Function<Tuple, Integer> strGrouping = s -> {
            if (s.getMyString().trim().length() > 0) {
                char firstLetter = s.getMyString().toLowerCase().trim().charAt(0);
                Integer result = 0;
                if (firstLetter <= 'g') {
                    result = 1;
                } else if (firstLetter >= 'h' && firstLetter <= 'n') {
                    result = 2;
                } else if (firstLetter >= 'o' && firstLetter <= 'u') {
                    result = 3;
                } else if (firstLetter >= 'v' && firstLetter <= 'z') {
                    result = 4;
                }
                return result;
            } else {
                return 0;
            }
        };

        List<String> allLines = new ArrayList<>();

        // this reads content from all files into an ArrayList of lines.
        // Potential OutOfMemory exception, if the files are huge
        try(Stream<Path> paths = Files.find(Paths.get("src/main/resources/input"), 10, (path, basicFileAttributes) -> {
            return !basicFileAttributes.isDirectory();
        })) {
            paths.forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    try {
                        allLines.addAll(Files.readAllLines(filePath));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // split lines into words, filter out white spaces
        List<String> allWords =  allLines
                .stream()
                .flatMap(l -> Arrays.stream(l.split("\\W+")))
                .filter(s -> s.trim().length() > 0)
                .collect(Collectors.toList());


        // map each word into a Tuple of type ("word", 1)
        Map<String, List<Tuple>> wordGroups = allWords
                .stream()
                .map(x -> new Tuple(x, 1))
                .collect(Collectors.groupingBy(Tuple::getMyString));

        // count all occurrences of the same word
        List<Tuple> wordsNumbers = wordGroups
                .entrySet()
                .stream()
                .map(g -> new Tuple(g.getKey(), g.getValue().size()))
                .collect(Collectors.toList());

        // partition all words into 4 groups, based on the function strGrouping
        Map<Integer, List<Tuple>> partitions = wordsNumbers
                .stream()
                .collect(Collectors.groupingBy(strGrouping));


        // write results into 4 separate files. Multiple PrintWriters look ugly, I know :)
        try (PrintWriter pw0 = new PrintWriter("src/main/resources/output/exceptions.txt", "UTF-8");
             PrintWriter pw1 = new PrintWriter("src/main/resources/output/AH.txt", "UTF-8");
             PrintWriter pw2 = new PrintWriter("src/main/resources/output/HN.txt", "UTF-8");
             PrintWriter pw3 = new PrintWriter("src/main/resources/output/OU.txt", "UTF-8");
             PrintWriter pw4 = new PrintWriter("src/main/resources/output/VZ.txt", "UTF-8");

        ) {
            Map<Integer, PrintWriter> writers = new HashMap<Integer, PrintWriter>();
            writers.put(0, pw0);
            writers.put(1, pw1);
            writers.put(2, pw2);
            writers.put(3, pw3);
            writers.put(4, pw4);

            partitions
                    .entrySet()
                    .stream()
                    .forEachOrdered( p ->
                            p.getValue()
                                    .stream()
                                    .forEach(writers.get(p.getKey())::println)
                    );
        }
    }

}
