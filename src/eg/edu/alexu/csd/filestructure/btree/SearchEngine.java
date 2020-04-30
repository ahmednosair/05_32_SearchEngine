package eg.edu.alexu.csd.filestructure.btree;

import org.xml.sax.SAXException;

import javax.management.RuntimeErrorException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class SearchEngine implements ISearchEngine {
    private IBTree<String, Map<String, Integer>> wordsTree;

    public SearchEngine(int BtreeParameter) {
        this.wordsTree = new BTree<>(BtreeParameter);
    }
    @Override
    public void indexWebPage(String filePath) {
        if (filePath == null||filePath.isEmpty()) {
            throw new RuntimeErrorException(new Error("\nInvalid file path!\n"));
        }
        try {
            List<Parser.Document> documents = Parser.parseFile(filePath);
            List<String> words;
            String id;
            for (Parser.Document doc : documents) {
                words = doc.getWords();
                id = doc.getId();
                for (String word : words) {
                    wordsTree.insert(word, new HashMap<>());
                    Map<String, Integer> wordList = wordsTree.search(word);
                    if (wordList.containsKey(id)) {
                        wordList.replace(id, wordList.get(id) + 1);
                    } else {
                        wordList.put(id, 1);
                    }
                }

            }

        } catch (IOException e) {
            throw new RuntimeErrorException(new Error("\nFile not found!\nPlease verify the file path.\n"));
        } catch (ParserConfigurationException | SAXException e) {
            throw new RuntimeErrorException(new Error("\nError parsing the xml document!\nPlease ensure that it is a valid xml document.\n"));
        }
    }

    @Override
    public void indexDirectory(String directoryPath) {
        if (directoryPath == null || directoryPath.isEmpty()) {
            throw new RuntimeErrorException(new Error("\nInvalid directory path!\n"));
        }
        ArrayList<String> files = getFiles(directoryPath);
        for (String filePath : files) {
            indexWebPage(filePath);
        }

    }

    private ArrayList<String> getFiles(String directoryPath) {
        ArrayList<String> files = new ArrayList<>();
        Queue<String> BFS = new LinkedList<>();
        File f = new File(directoryPath);
        if (f.isDirectory()) {
            BFS.add(directoryPath);
        } else {
            files.add(f.getAbsolutePath());
        }
        while (!BFS.isEmpty()) {
            f = new File(BFS.poll());
            if (f.isDirectory()) {
                File[] subs = f.listFiles();
                if (subs != null) {
                    for (File sub : subs) {
                        if (sub.isDirectory()) {
                            BFS.add(sub.getAbsolutePath());
                        } else {
                            files.add(sub.getAbsolutePath());
                        }
                    }
                }
            } else {
                files.add(f.getAbsolutePath());
            }
        }
        return files;
    }

    @Override
    public void deleteWebPage(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            throw new RuntimeErrorException(new Error("\nInvalid file path!\n"));
        }
        try {
            List<Parser.Document> documents = Parser.parseFile(filePath);
            List<String> words;
            String id;
            for (Parser.Document doc : documents) {
                words = doc.getWords();
                id = doc.getId();
                for (String word : words) {
                    wordsTree.insert(word, new HashMap<>());
                    Map<String, Integer> wordList = wordsTree.search(word);
                    wordList.remove(id);
                }
            }

        } catch (IOException e) {
            throw new RuntimeErrorException(new Error("\nFile not found!\nPlease verify the file path.\n"));
        } catch (ParserConfigurationException | SAXException e) {
            throw new RuntimeErrorException(new Error("\nError parsing the xml document!\nPlease ensure that it is a valid xml document.\n"));
        }

    }

    @Override
    public List<ISearchResult> searchByWordWithRanking(String word) {
        if (word == null) {
            throw new RuntimeErrorException(new Error("\nCan't search for a null word!\n"));
        }
        return outputResult(wordsTree.search(word.toLowerCase()));
    }

    @Override
    public List<ISearchResult> searchByMultipleWordWithRanking(String sentence) {
        if (sentence == null) {
            throw new RuntimeErrorException(new Error("\nInvalid sentence!\n"));
        }
        String[] queryWords = sentence.replaceAll("\\s+", " ").toLowerCase().split(" ");
        if (queryWords.length == 0) {
            return Collections.emptyList();
        }
        Map<String, Integer> candidates = wordsTree.search(queryWords[0]);
        Map<String, Integer> tmp;
        if (candidates == null) {
            return Collections.emptyList();
        }
        for (int i = 1; i < queryWords.length; i++) {
            Map<String, Integer> intersection = new HashMap<>();
            tmp = wordsTree.search(queryWords[i]);
            if (tmp == null) {
                return Collections.emptyList();
            }
            if (tmp.size() < candidates.size()) {
                for (Map.Entry<String, Integer> entry : tmp.entrySet()) {
                    if (candidates.containsKey(entry.getKey())) {
                        intersection.put(entry.getKey(), Math.min(entry.getValue(), candidates.get(entry.getKey())));
                    }
                }
            } else {
                for (Map.Entry<String, Integer> entry : candidates.entrySet()) {
                    if (tmp.containsKey(entry.getKey())) {
                        intersection.put(entry.getKey(), Math.min(entry.getValue(), tmp.get(entry.getKey())));
                    }
                }
            }
            candidates = intersection;
        }
        return outputResult(candidates);
    }

    private List<ISearchResult> outputResult(Map<String, Integer> result) {
        List<ISearchResult> output = new ArrayList<>();
        if (result == null) {
            return output;
        }
        for (Map.Entry<String, Integer> entry : result.entrySet()) {
            output.add(new SearchResult(entry.getKey(), entry.getValue()));
        }
        return output;
    }
}
