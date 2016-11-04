package com.jeetatl.datamasking.config;

import com.jeetatl.datamasking.MaskingSetting;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mkhokhar on 11/3/16.
 */
public class Configuration {

    private Map<String, MaskingSetting> fieldConfiguration;
    private String configString;
    private static Pattern configPattern;
    private static Pattern configSubPattern;
    private static final String PLUS_SIGN = "+";
    private static final String MINUS_SIGN = "-";
    private static final String CONFIG_PATTERN_STRING = "((\\w+):(([-+]?\\d+%?)(,[-+]?\\d+%?)*))";
    private static final String CONFIG_PATTERN_SUB_STRING = "([-+])?(\\d+)(%)?";

    public Configuration(String configString) {
        fieldConfiguration = new HashMap<String, MaskingSetting>();
        configPattern = Pattern.compile(CONFIG_PATTERN_STRING);
        configSubPattern = Pattern.compile(CONFIG_PATTERN_SUB_STRING);
        this.configString = configString;
        initialize();
    }

    private void initialize() {
        Matcher matcher = configPattern.matcher(configString);
        while(matcher.find()) {
//            System.out.println("group count: " + matcher.groupCount());
//            for (int i = 0; i <= matcher.groupCount(); i++) {
//                System.out.println("Group: [" + i + "]: " + matcher.group(i));
//            }
            Matcher subMatcher = configSubPattern.matcher(matcher.group(3));
            MaskingSetting ms = new MaskingSetting();
            while (subMatcher.find()) {
//                System.out.println("-------------------------");
//                System.out.println("Field name: " + matcher.group(2));
//                System.out.println("Complete group: " + subMatcher.group(0));
//                System.out.println("Sign: " + subMatcher.group(1));
//                System.out.println("Magnitude: " + subMatcher.group(2));
//                System.out.println("Percent?: " + subMatcher.group(3));
//                System.out.println("-------------------------");

                String sign = subMatcher.group(1);
                String magnitude = subMatcher.group(2);
                boolean isPercentage = subMatcher.group(3) == null ? false : true;

                if (sign == null || sign.equals(PLUS_SIGN)) {
                    if (isPercentage) {
                        ms.setPercentMaskLeft(Double.parseDouble(magnitude));
                    } else {
                        ms.setCharactersMaskLeft(Integer.parseInt(magnitude));
                    }
                } else {
                    if (isPercentage) {
                        ms.setPercentMaskRight(Double.parseDouble(magnitude));
                    } else {
                        ms.setCharactersMaskRight(Integer.parseInt(magnitude));
                    }
                }
                fieldConfiguration.put(matcher.group(2), ms);

            }

        }
        System.out.println(fieldConfiguration);
    }

    public static void main(String[] args) {
        Configuration config = new Configuration("apple:50%,5|orange:+20|banana:+50%,-50%,5,+8,-5");
//        Configuration config = new Configuration("apple:50%,5,-5,-30%");
    }
}
