package main.controllers;

import main.request.URISiteRequest;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

@Controller
public class MainController {

    @GetMapping("/")
    public String getIndex(Model model)
    {
        model.addAttribute("allWordsString", "here will shows the text");
        return "index";
    }

    @ResponseBody
    @PostMapping("/push")
    public TreeMap<String, Integer> mainMethod(@RequestBody URISiteRequest request, Model model)
    {
        String uriOfSite = request.getUriOfSiteString();
        System.out.println("uriOfSite - " + uriOfSite);
        Document domDocument = null;
        if (uriOfSite == null || uriOfSite.equals(""))
        {
            uriOfSite = "http://www.testbase.ru/test-it";
        }
        try
        {
            domDocument = connectToSite(uriOfSite);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        System.out.println(domDocument.title());
//        System.out.println(domDocument.body().text());
        String longString = domDocument.body().text();
        String lowerCasedLongString = longString.toLowerCase();
        String[] arrayFromString = lowerCasedLongString.split("");
        TreeMap<String, Integer> mapOfAllWords = new TreeMap<>();

        String[] specSymbols = {".", ",", ";", ":", "\"", "?",
                "!", "$", "#", "%", "*", "<", ">", "{", "}",
                "[", "]", "—", "-", "(", ")"};
        int[] sequenceForSymbols = new int[specSymbols.length];
        for (int i = 0; i < specSymbols.length; i++)
        {
            sequenceForSymbols[i] = 0;
        }

        for (String s : arrayFromString)
        {
            for (int j = 0; j < specSymbols.length; j++)
            {
                if (s.equals(specSymbols[j]))
                {
                    sequenceForSymbols[j]++;
                }
            }
        }

        for (int i = 0; i < sequenceForSymbols.length; i++)
        {
            if (sequenceForSymbols[i] != 0)
            {
                System.out.println("\"" + specSymbols[i] + "\" -- " + sequenceForSymbols[i] + " times founded.");
            }
        }

        String clearedString = lowerCasedLongString;
        for (int i = 0; i < specSymbols.length; i++)
        {
            clearedString = clearedString.replace(specSymbols[i], "");
        }

        System.out.println(clearedString);
        String[] wordsArr = clearedString.split(" ");

        int value = 0;
        for (String s : wordsArr)
        {
            if (mapOfAllWords.containsKey(s))
            {
                value = mapOfAllWords.get(s);
                mapOfAllWords.put(s, ++value);
            } else {
                mapOfAllWords.put(s, 1);
            }
        }

        mapOfAllWords.forEach((key, val) -> {
            System.out.println(key + " - " + val);
        });
        System.out.println("Total different words = " + mapOfAllWords.size());

        StringBuilder stringBuilder = new StringBuilder();

        for(Map.Entry<String,Integer> entry : mapOfAllWords.entrySet()) {
            String key = entry.getKey();
            Integer value1 = entry.getValue();
            stringBuilder.append(key + " - " + value1 + " times founded.\n");
        }
        model.addAttribute("allWordsString", stringBuilder.toString());

        model.addAttribute("allWords", mapOfAllWords);
        return mapOfAllWords;
        /**
         * todo:
         * нужно перечислить все спец симоволы и найти их колличество в тексте.
         * найти в тексте по паттерну сайта (example.com или http://example.com) и отдельно их посчитать и сохранить
         * Нати паттерны почты в тексте и тоже отдельно их посчитать и сохранить
         * Найти все отдельные слова заменив все символы на пробелы
         *
         */
    }

    private Document connectToSite(String url) throws IOException
    {
        return Jsoup.connect(url)
                .userAgent("Chrome/4.0.249.0 Safari/532.5")
                .referrer("google.com")
                .maxBodySize(0)
                .timeout(15000)
                .get();
    }

}
