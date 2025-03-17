package com.conduit.common.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class SlugUtil {
    private static  final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static  final Pattern WHITESPACE = Pattern.compile("\\s");

    public static String toSlug(String input) {
        if(input == null || input.isEmpty()) {
            return "";
        }
        return NON_LATIN.matcher(
                WHITESPACE.matcher(
                        Normalizer.normalize(input, Normalizer.Form.NFD)
                ).replaceAll("-")
        ).replaceAll("").toLowerCase(Locale.ENGLISH);
   }

    public static String fromSlug(String slug) {
        return slug.replace("-", " ");
    }
}
