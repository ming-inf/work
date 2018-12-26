package util;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.ibm.icu.util.ULocale;

public class Misc {
  public void printAvailableLocales() {
    Set<String> locales = Arrays
        .stream(ULocale.getAvailableLocales())
        .filter(l -> !l.getCountry().isEmpty())
        .map(l -> String.format("%s: %s, %s; %s, %s", l, l.getDisplayLanguage(), l.getDisplayCountry(), l.getDisplayLanguage(l), l.getDisplayCountry(l)))
        .collect(Collectors.toCollection(TreeSet::new));
    locales.forEach(System.out::println);
  }
}
