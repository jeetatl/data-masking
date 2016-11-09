package com.jeetatl.datamasking.config;

import com.jeetatl.datamasking.MaskingSetting;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>{@code MaskingConfiguration} is a concrete class that stores
 * the masking configuration of fields defined by the user. It is
 * used to apply masking to different xml and json fields. This is
 * useful for logging the requests and responses that contain PII
 * (Personally identifiable information), PCI (Payment Card Industry),
 * or other sensitive data.</p>
 *
 * <p>{@code MaskingConfiguration} uses a special format to mask the
 * beginning and/or end of the field.  The masking amount can be provided
 * in terms of number of characters or percentage.</p>
 *
 * <p>The masking strategies consist of the following:</p>
 * <ol>
 * <li>Mask X characters from the beginning of field.</li>
 * <li>Mask X percent of characters from the beginning of field.</li>
 * <li>Mask X characters from the end of field.</li>
 * <li>Mask X percent of characters from the end of field.</li>
 * <li>Mask ranges from middle of field.</li>
 * <li>Apply any combination of above masks to a field.</li>
 * </ol>
 *
 * <p>The format of configuration string consists of following format: </p>
 * <ul>
 * <li>fieldName:maskingSetting1,maskingSetting2|fieldName2:maskingSetting1,maskingSetting2|...</li>
 * </ul>
 *
 *
 * <table border=0 cellspacing=3 cellpadding=0 summary="Table shows examples of masking settings and their effects.">
 * <tr style="background-color: rgb(161, 243, 233);">
 * <th>Configuration</th>
 * <th>Field Name</th>
 * <th>Masking Settings</th>
 * <th>Original Value</th>
 * <th>Masked Value</th>
 * </tr>
 * <tr style="background-color: rgb(238, 255, 253);">
 * <td>cardNumber:~7-12</td>
 * <td>cardNumber</td>
 * <td>~7-12</td>
 * <td>1234567890123456</td>
 * <td>123456XXXXXX3456</td>
 * </tr>
 * <tr>
 * <td>fieldName:+5,-3</td>
 * <td>fieldName</td>
 * <td>+5,-3</td>
 * <td>1234567890</td>
 * <td>XXXXX67XXX</td>
 * </tr>
 * <tr style="background-color: rgb(238, 255, 253);">
 * <td>fieldName:+30%</td>
 * <td>fieldName</td>
 * <td>+30%</td>
 * <td>1234567890</td>
 * <td>XXX4567890</td>
 * </tr>
 * <tr>
 * <td>fieldName:-30%</td>
 * <td>fieldName</td>
 * <td>-30%</td>
 * <td>1234567890</td>
 * <td>1234567XXX</td>
 * </tr>
 * <tr style="background-color: rgb(238, 255, 253);">
 * <td>fieldName:+50%,-30%</td>
 * <td>fieldName</td>
 * <td>+50%,-30%</td>
 * <td>1234567890</td>
 * <td>XXXXX67XXX</td>
 * </tr>
 * <tr>
 * <td>fieldName:+5,~7,~9-10,-30%</td>
 * <td>fieldName</td>
 * <td>+5,~7,~9-10,-30%</td>
 * <td>12345678901234567890</td>
 * <td>XXXXX6X8XX1234XXXXXX</td>
 * </tr>
 * </table>
 *
 * <p>Any {@code MaskingSetting} string may be used for default masking.</p>
 *
 * @author Mohammad Ali Khokhar
 * @since 0.1.0
 */
public class MaskingConfiguration {
    private static final MaskingSetting DEFAULT_MASKING_SETTING = new MaskingSetting();
    private static final boolean IS_ATTR_MASKED_DEFAULT = true;

    private static final String PLUS_SIGN = "+";
    private static final String TILDE_SYMBOL = "~";
    private static final String RANGE_SEPARATOR = "-";
    private static final String CONFIG_PATTERN_STRING = "(\\w+):(([-+~]\\d+(-\\d+)?%?,?)+)";
    private static final String OUTER_MASKING_PATTERN_STR = "(^[-+]|,[-+])(\\d+)(%)?";
    private static final String INNER_MASKING_PATTERN_STR = "(~)((\\d+)(-\\d+)?)";
    private static final Pattern configPattern = Pattern.compile(CONFIG_PATTERN_STRING);
    private static final Pattern outerMaskingPattern = Pattern.compile(OUTER_MASKING_PATTERN_STR);
    private static final Pattern innerMaskingPattern = Pattern.compile(INNER_MASKING_PATTERN_STR);

    private MaskingSetting unknownFieldMasking = DEFAULT_MASKING_SETTING;
    private Map<String, MaskingSetting> fieldConfiguration;
    private String configString;
    private boolean maskXMLAttributes = IS_ATTR_MASKED_DEFAULT;

    /**
     * Constructs the {@code MaskingConfiguration} with defaul configuration settings. It
     * defaults masking for fields that are not found in the configuration string to
     * no masking.  None of the fields are masked unless {@link #setConfigString} is used
     * to assing a new configuration string.
     */
    public MaskingConfiguration() {
        initialize();
    }


    /**
     * Constructs the {@code MaskingConfiguration} from configuration string. It
     * defaults masking for fields that are not found in the configuration string to
     * no masking.
     * @param configString A string containing the fields and their masking configuration.
     */
    public MaskingConfiguration(String configString) {
        this.configString = configString;
        initialize();
    }

    /**
     * Constructs the {@code MaskingConfiguration} from configuration string. It
     * also sets the default masking for fields that are not found in the
     * configuration string.
     * @param configString A string containing the fields and their masking configuration.
     * @param defaultMasking A string containing masking settings string that will be
     *                       applied to fields that are not found in field configuration
     *                       map.
     */
    public MaskingConfiguration(String configString, String defaultMasking) {
        MaskingSetting ms = createMaskingSetting(defaultMasking);
        this.unknownFieldMasking = ms;
        this.configString = configString;
        initialize();
    }

    /**
     * Parses the configuration string and creates a fields configuration map.
     */
    private void initialize() {
        fieldConfiguration = new TreeMap<>();

        if (configString == null || configString.length() == 0) {
            return;
        }

        Matcher matcher = configPattern.matcher(configString);
        while (matcher.find()) {
            MaskingSetting ms = createMaskingSetting(matcher.group(2));
            fieldConfiguration.put(matcher.group(1), ms);
        }
    }

    /**
     * Sets the configuration string for the masking.
     * @param configString A string containing the fields and their masking configuration.
     */
    public void setConfigString(String configString) {
        this.configString = configString;
        initialize();
    }

    /**
     * The {@code apply} method is used to retrieve {@code MaskingSetting}
     * for the field passed in and applies to {@code String} that
     * is passed in.  It returns a new {@code String} with the settings applied.
     * @param fieldName The field name who's settings should be applied.
     * @param value The {@code String} to which the settings should be applied.
     * @return The {@code String} after the masking settings have been applied.
     */
    public String apply(String fieldName, String value) {
        if (value == null || value.length() == 0) {
            return value;
        }
        StringBuffer sb = new StringBuffer(value);
        apply(fieldName, sb);
        return sb.toString();
    }

    /**
     * The {@code apply} method is used to retrieve {@code MaskingSetting}
     * for the field passed in and applies to {@code StringBuilder} that
     * is passed in.
     * @param fieldName The field name who's settings should be applied.
     * @param sb The {@code StringBuilder} to which the settings should be applied.
     */
    public void apply(String fieldName, StringBuilder sb) {
        if (fieldConfiguration.containsKey(fieldName)) {
            fieldConfiguration.get(fieldName).apply(sb);
        } else {
            unknownFieldMasking.apply(sb);
        }
    }

    /**
     * The {@code apply} method is used to retrieve {@code MaskingSettin}g
     * for the field passed in and applies to {@code StringBuffer} that
     * is passed in.
     * @param fieldName The field name who's settings should be applied.
     * @param sb The {@code StringBuffer} to which the settings should be applied.
     */
    public void apply(String fieldName, StringBuffer sb) {
        if (fieldConfiguration.containsKey(fieldName)) {
            fieldConfiguration.get(fieldName).apply(sb);
        } else {
            unknownFieldMasking.apply(sb);
        }
    }

    /**
     * Search the configuration to see if a masking setting is available for a particular field.
     * @param fieldName The field name to search the configuration setting for.
     * @return true if a {@code MaskingSetting} is found for the {@code fieldName}, false otherwise.
     */
    public boolean containsMaskingSettingForField(String fieldName) {
        return fieldConfiguration.containsKey(fieldName);
    }

    /**
     * <p>A helper method to parse a string containing a configuration for a field.</p>
     * @param pattern A string containing the pattern for masking.
     * @return {@code MaskingSetting} object that contains the parsed setting.
     */
    private MaskingSetting createMaskingSetting(String pattern) {
        Matcher outerMatcher = outerMaskingPattern.matcher(pattern);
        Matcher innerMatcher = innerMaskingPattern.matcher(pattern);

        MaskingSetting ms = new MaskingSetting();

        while (outerMatcher.find()) {
            String symbol = outerMatcher.group(1);
            String magnitude = outerMatcher.group(2);
            boolean isPercentage = outerMatcher.group(3) == null ? false : true;
            addToMaskingSetting(ms, symbol, magnitude, isPercentage);
        }

        while (innerMatcher.find()) {
            String maskingRange = innerMatcher.group(2);
            addToMaskingSetting(ms, TILDE_SYMBOL, maskingRange, false);
        }

        return ms;
    }

    /**
     * <p>A helper method for adding settings to {@code MaskingSetting}.</p>
     * @param ms {@code MaskingSetting} to which to add settings to.
     * @param symbol A symbol to determine if the masking is applied to left,
     *               right, or middle
     * @param maskingRange The magnitude or range of values to mask.
     * @param isPercentage Flag to determine if the magnitude passed in is percentage.
     */
    private void addToMaskingSetting(MaskingSetting ms, String symbol, String maskingRange, boolean isPercentage) {
        if (TILDE_SYMBOL.equals(symbol)) {
            if (maskingRange.contains(RANGE_SEPARATOR)) {
                String[] tokens = maskingRange.split(RANGE_SEPARATOR);
                ms.addInnerMasking(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
            } else {
                ms.addInnerMasking(Integer.parseInt(maskingRange), Integer.parseInt(maskingRange));
            }
        } else {
            if (symbol == null || symbol.equals(PLUS_SIGN)) {
                if (isPercentage) {
                    ms.setPercentMaskLeft(Double.parseDouble(maskingRange));
                } else {
                    ms.setCharactersMaskLeft(Integer.parseInt(maskingRange));
                }
            } else {
                if (isPercentage) {
                    ms.setPercentMaskRight(Double.parseDouble(maskingRange));
                } else {
                    ms.setCharactersMaskRight(Integer.parseInt(maskingRange));
                }
            }
        }
    }

    /**
     * Return the configuration for masking attributes of elements.
     * @return Returns the configuration for masking attributes of elements.
     */
    public boolean isAttributesMaskEnabled() {
        return maskXMLAttributes;
    }

    /**
     * Set the configuration for masking attributes of element.  Default: true.
     * @param bool Set the configuration for masking attributes of elements.
     */
    public void setAttributeMaskEnabled(boolean bool) {
        maskXMLAttributes = bool;
    }
}
