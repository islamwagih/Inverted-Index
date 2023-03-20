import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main
{
    public static String preprocess(String word)
    {
        word = word.toLowerCase(Locale.ROOT);
        String newWord = "";
        int len = word.length(), pos = 1;
        for(char ch:word.toCharArray())
        {
            if((ch >= 'a' && ch <= 'z'))
            {
                newWord += ch;
            }
            pos++;
        }
        return newWord;
    }

    public static Set<String> listFiles(String dir) {
        return Stream.of(new File(dir).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
    }

    public static ArrayList<String> findLines(String fileName) {
        String WORD_FILE = fileName;
        ArrayList<String> words = new ArrayList<>();
        try {
            File file = new File(WORD_FILE);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext() == true) {
                String word = scanner.next();
                word = preprocess(word);
                if(word.length() > 0) words.add(word);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: File not found.");
        }
        return words;
    }

    public static int parseDocId(String fileName)
    {
        String strId = "";
        boolean underScore = false;
        for(char ch:fileName.toCharArray())
        {
            if(ch == '_')
            {
                underScore = true;
                continue;
            }
            if(ch == '.') break;
            if(underScore) strId += ch;
        }
        return Integer.parseInt(strId);
    }


    public static void main(String[] args)
    {
        Set<String> files = listFiles("Docs");
        HashMap<String, DictEntry> invertedIndex = new HashMap<>();
        for(String fileName:files)
        {
            int docId = parseDocId(fileName);
            ArrayList<String> words = findLines("D:/Projects/InvertedIndex/Docs/"+fileName);
            for(String word:words)
            {
                DictEntry entry;
                if(!invertedIndex.containsKey(word)) {
                    entry = new DictEntry();
                }else {
                    entry = invertedIndex.get(word);
                }
                entry.add(docId);
                invertedIndex.put(word, entry);
            }
        }

        try (PrintWriter printer = new PrintWriter(new FileOutputStream("InvertedIndexAnalytics.txt", false))) {
            for (String key : invertedIndex.keySet())
            {
                printer.println("----------------------------");
                printer.println("key: "+key);
                for(Pair pair:invertedIndex.get(key).getDocsIdAndFrq())
                {
                    printer.println("Doc id: "+pair.getDocId()+ ", Frq: "+pair.getDocFrq());
                }
                printer.println("----------------------------");
            }

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }



    }
}
