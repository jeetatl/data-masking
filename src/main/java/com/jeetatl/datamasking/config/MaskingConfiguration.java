package com.jeetatl.datamasking.config;

import com.jeetatl.datamasking.MaskingSetting;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mkhokhar on 11/3/16.
 */
public class MaskingConfiguration {
    private static final MaskingSetting DEFAULT_MASKING_SETTING = new MaskingSetting();

    private static final String PLUS_SIGN = "+";
    private static final String MINUS_SIGN = "-";
    private static final String CONFIG_PATTERN_STRING = "((\\w+):(([-+]?\\d+%?)(,[-+]?\\d+%?)*))";
    private static final String CONFIG_PATTERN_SUB_STRING = "([-+])?(\\d+)(%)?";
    private static final Pattern configPattern = Pattern.compile(CONFIG_PATTERN_STRING);
    private static final Pattern configSubPattern = Pattern.compile(CONFIG_PATTERN_SUB_STRING);

    private MaskingSetting unknownFieldMasking = DEFAULT_MASKING_SETTING;
    private Map<String, MaskingSetting> fieldConfiguration;
    private String configString;

    public MaskingConfiguration() {
        initialize();
    }

    public MaskingConfiguration(String configString) {
        this.configString = configString;
        initialize();
    }

    public MaskingConfiguration(String configString, String defaultMasking) {
        MaskingSetting ms = createMaskingSetting(defaultMasking);
        this.unknownFieldMasking = ms;
        this.configString = configString;
        initialize();
    }

    private void initialize() {
        fieldConfiguration = new HashMap<String, MaskingSetting>();

        if (configString == null || configString.length() == 0) {
            return;
        }

        Matcher matcher = configPattern.matcher(configString);
        while(matcher.find()) {
            MaskingSetting ms = createMaskingSetting(matcher.group(3));
            fieldConfiguration.put(matcher.group(2), ms);
        }
    }

    public void setConfigString(String configString) {
        this.configString = configString;
        initialize();
    }

    public String apply(String fieldName, String value) {
        if (value == null || value.length() == 0) {
            return value;
        }
        StringBuffer sb = new StringBuffer(value);
        apply(fieldName, sb);
        return sb.toString();
    }

    public void apply(String fieldName, StringBuilder sb) {
        if (fieldConfiguration.containsKey(fieldName)) {
            fieldConfiguration.get(fieldName).apply(sb);
        } else {
            unknownFieldMasking.apply(sb);
        }
    }

    public void apply(String fieldName, StringBuffer sb) {
        if (fieldConfiguration.containsKey(fieldName)) {
            fieldConfiguration.get(fieldName).apply(sb);
        } else {
            unknownFieldMasking.apply(sb);
        }
    }

    private MaskingSetting createMaskingSetting(String pattern) {
        Matcher subMatcher = configSubPattern.matcher(pattern);

        MaskingSetting ms = new MaskingSetting();
        while (subMatcher.find()) {
            String sign = subMatcher.group(1);
            String magnitude = subMatcher.group(2);
            boolean isPercentage = subMatcher.group(3) == null ? false : true;

            addToMaskingSetting(ms, sign, magnitude, isPercentage);
        }

        return ms;
    }

    private void addToMaskingSetting(MaskingSetting ms, String sign, String magnitude, boolean isPercentage) {
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
    }
}
