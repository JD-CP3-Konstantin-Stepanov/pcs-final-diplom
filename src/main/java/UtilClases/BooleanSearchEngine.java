package UtilClases;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    SearchEngineMap searchEngineMap;

    public BooleanSearchEngine(File FilePDF, SearchEngineMap searchEngineMap, List<String> stopWordsList) {
        this.searchEngineMap = searchEngineMap;
        String file = FilePDF.getName();
        try {
            PdfReader reader = new PdfReader(FilePDF.getPath());
            StringBuilder textBuilder;
            for (int page = 1; page <= reader.getNumberOfPages(); page++) {
                textBuilder = new StringBuilder();
                try {
                    String textExtracted = String.valueOf(textBuilder
                            .append(PdfTextExtractor.getTextFromPage(reader, page)));

                    String[] wordArray = textExtracted.toLowerCase().split("\\P{IsAlphabetic}+");
                    Map<String, Integer> freq = new HashMap<>();
                    for (var word : wordArray) {

                        String currentWord = word;
                        String result = stopWordsList.stream().filter(searchVal -> searchVal
                                .equals(currentWord)).findAny().orElse(null);

                        if (word.isEmpty() || !Objects.equals(result, null)) {
                            continue;
                        }
                        word = word.toLowerCase();
                        freq.put(word, freq.getOrDefault(word, 0) + 1);
                    }
                    for (Map.Entry<String, Integer> entry : freq.entrySet()) {
                        searchEngineMap.searchEngineMapAdd(entry.getKey(),
                                new SearchEngineEntry(file, page, entry.getValue()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<PageEntry> search(String[] words) {
        List<SearchEngineEntry> engineEntryListlist;
        List<PageEntry> pageEntryList = new ArrayList<>();
        for (String wordValue : words) {
            engineEntryListlist = searchEngineMap.getSearchEngineMap().get(wordValue);
            if (engineEntryListlist != null) {
                for (SearchEngineEntry searchEngineValue : engineEntryListlist) {
                    boolean flagUpdate = false;
                    for (int i = 0; i < pageEntryList.size(); i++) {
                        if (pageEntryList.get(i).getPdfName()
                                .equals(searchEngineValue.getFileName())
                                && pageEntryList.get(i).getPage() == (searchEngineValue.getPageNumber())) {
                            int freq = pageEntryList.get(i).getCount();
                            pageEntryList.set(i, new PageEntry(searchEngineValue.getFileName(),
                                    searchEngineValue.getPageNumber(), searchEngineValue.getFreq() + freq));
                            flagUpdate = true;
                            break;
                        }
                    }
                    if (!flagUpdate) {
                        pageEntryList.add(new PageEntry(searchEngineValue.getFileName(),
                                searchEngineValue.getPageNumber(), searchEngineValue.getFreq()));
                    }
                }
            }
        }
        Collections.sort(pageEntryList);
        return pageEntryList;
    }
}
