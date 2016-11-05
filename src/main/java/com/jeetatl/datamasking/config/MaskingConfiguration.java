package com.jeetatl.datamasking.config;

import com.jeetatl.datamasking.MaskingSetting;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p><code>MaskingConfiguration</code> is a concrete class that stores
 * the masking configuration of fields defined by the user. It is
 * used to apply masking to different xml and json fields. This is
 * useful for logging the requests and responses that contain PII
 * (Personally identifiable information), PCI (Payment Card Industry),
 * or other sensitive data.</p>
 *
 * <p><code>MaskingConfiguration</code> uses a special format to mask the
 * beginning and/or end of the field.  The masking amount can be provided
 * in terms of number of characters or percentage.</p>
 *
 * The masking strategies consist of the following:
 * <ol>
 * <li>Mask X characters from the beginning of field.</li>
 * <li>Mask X percent of characters from the beginning of field.</li>
 * <li>Mask X characters from the end of field.</li>
 * <li>Mask X percent of characters from the end of field.</li>
 * <li>Mask ranges from middle of field.</li>
 * <li>Apply any combination of above masks to a field.</li>
 * </ol>
 *
 * <br/>
 * The format of configuration string consists of following format:
 * <ul>
 *     <li>fieldName:maskingSetting1,maskingSetting2|fieldName2:maskingSetting1,maskingSetting2|...</li>
 * </ul>
 * <br/>
 *
 * <table border=0 cellspacing=3 cellpadding=0>
 *     <tr style="background-color: rgb(161, 243, 233);">
 *         <th>Configuration</th>
 *         <th>Field Name</th>
 *         <th>Masking Settings</th>
 *         <th>Original Value</th>
 *         <th>Masked Value</th>
 *     </tr>
 *     <tr style="background-color: rgb(238, 255, 253);">
 *         <td>cardNumber:~7-12</td>
 *         <td>cardNumber</td>
 *         <td>~7-12</td>
 *         <td>1234567890123456</td>
 *         <td>123456XXXXXX3456</td>
 *     </tr>
 *     <tr>
 *         <td>fieldName:+5,-3</td>
 *         <td>fieldName</td>
 *         <td>+5,-3</td>
 *         <td>1234567890</td>
 *         <td>XXXXX67XXX</td>
 *     </tr>
 *     <tr style="background-color: rgb(238, 255, 253);">
 *         <td>fieldName:+30%</td>
 *         <td>fieldName</td>
 *         <td>+30%</td>
 *         <td>1234567890</td>
 *         <td>XXX4567890</td>
 *     </tr>
 *     <tr>
 *         <td>fieldName:-30%</td>
 *         <td>fieldName</td>
 *         <td>-30%</td>
 *         <td>1234567890</td>
 *         <td>1234567XXX</td>
 *     </tr>
 *     <tr style="background-color: rgb(238, 255, 253);">
 *         <td>fieldName:+50%,-30%</td>
 *         <td>fieldName</td>
 *         <td>+50%,-30%</td>
 *         <td>1234567890</td>
 *         <td>XXXXX67XXX</td>
 *     </tr>
 *     <tr>
 *         <td>fieldName:+5,~7,~9-10,-30%</td>
 *         <td>fieldName</td>
 *         <td>+5,~7,~9-10,-30%</td>
 *         <td>12345678901234567890</td>
 *         <td>XXXXX6X8XX1234XXXXXX</td>
 *     </tr>
 * </table>
 *
 * @author Mohammad Ali Khokhar
 * @since 0.1.0
 */
public class MaskingConfiguration {
    private static final MaskingSetting DEFAULT_MASKING_SETTING = new MaskingSetting();

    private static final String PLUS_SIGN = "+";
    private static final String CONFIG_PATTERN_STRING = "(\\w+):(([-+]?\\d+%?)(,[-+]?\\d+%?)*)";
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

    /**
     * @param configString
     * @param defaultMasking
     */
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
            MaskingSetting ms = createMaskingSetting(matcher.group(2));
            fieldConfiguration.put(matcher.group(1), ms);
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
