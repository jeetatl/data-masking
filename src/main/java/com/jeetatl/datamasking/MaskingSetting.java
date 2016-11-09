package com.jeetatl.datamasking;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>{@code MaskingSetting} is a concrete class that holds the settings for masking.</p>
 *
 * <p>Masking settings consist of following:</p>
 * <table border=0 cellspacing=3 cellpadding=0 summary="Table shows examples of masking settings and their effects.">
 *     <tr style="background-color: rgb(161, 243, 233);">
 *         <th>Masking Setting</th>
 *         <th>Description</th>
 *         <th>Original Value</th>
 *         <th>Masked Value</th>
 *     </tr>
 *     <tr>
 *         <td>+5</td>
 *         <td>Mask 5 characters from left side of value</td>
 *         <td>1234567890</td>
 *         <td>XXXXX67890</td>
 *     </tr>
 *     <tr style="background-color: rgb(238, 255, 253);">
 *         <td>-5</td>
 *         <td>Mask 5 characters from right side of value</td>
 *         <td>1234567890</td>
 *         <td>12345XXXXX</td>
 *     </tr>
 *     <tr>
 *         <td>+15%</td>
 *         <td>Mask 15% of characters from left side of value (rounded to next character).</td>
 *         <td>1234567890</td>
 *         <td>XX34567890</td>
 *     </tr>
 *     <tr style="background-color: rgb(238, 255, 253);">
 *         <td>-15%</td>
 *         <td>Mask 15% of characters from right side of value (rounded to next character).</td>
 *         <td>1234567890</td>
 *         <td>12345678XX</td>
 *     </tr>
 *     <tr>
 *         <td>~5</td>
 *         <td>Mask 5th character of value.</td>
 *         <td>1234567890</td>
 *         <td>1234X67890</td>
 *     </tr>
 *     <tr style="background-color: rgb(238, 255, 253);">
 *         <td>~5-7</td>
 *         <td>Mask characters 5-7 (inclusive) of value.</td>
 *         <td>1234567890</td>
 *         <td>1234XXX890</td>
 *     </tr>
 *     <tr>
 *         <td>+2,~5,-30%</td>
 *         <td>
 *             Mix and match settings:
 *             <ul>
 *                 <li>Mask 2 characters from the left side of value.</li>
 *                 <li>Mask 5th character of value.</li>
 *                 <li>Mask 30% of characters from the right side of value.</li>
 *             </ul>
 *         </td>
 *         <td>1234567890</td>
 *         <td>XX34X67XXX</td>
 *     </tr>
 *     <tr style="background-color: rgb(238, 255, 253);">
 *         <td>+25%,~5,~7,-20%</td>
 *         <td>
 *             Mix and match settings:
 *             <ul>
 *                 <li>Mask 25% of characters from the left side of value.</li>
 *                 <li>Mask 5th and 7th character of value.</li>
 *                 <li>Mask 20% of characters from the right side of value.</li>
 *             </ul>
 *         </td>
 *         <td>1234567890</td>
 *         <td>XXX4X6X8XX</td>
 *     </tr>
 * </table>
 *
 * @author Mohammad Ali Khokhar
 * @since 0.1.0
 */


public class MaskingSetting {
    private static final int DEFAULT_CHARACTERS_MASK_LEFT = 0;
    private static final int DEFAULT_CHARACTERS_MASK_RIGHT = 0;
    private static final double DEFAULT_PERCENTAGE_MASK_LEFT = 0;
    private static final double DEFAULT_PERCENTAGE_MASK_RIGHT = 0;
    private static final char DEFAULT_MASKING_CHARACTER = 'X';

    private int charactersMaskLeft = DEFAULT_CHARACTERS_MASK_LEFT;
    private int charactersMaskRight = DEFAULT_CHARACTERS_MASK_RIGHT;
    private double percentMaskLeft = DEFAULT_PERCENTAGE_MASK_LEFT;
    private double percentMaskRight = DEFAULT_PERCENTAGE_MASK_RIGHT;
    private char maskingCharacter = DEFAULT_MASKING_CHARACTER;

    // TODO: add inner masking logic
    private List<int[]> innerMasks = new ArrayList<int[]>();

    /**
     * Construct {@code MaskingSetting} with default settings.
     */
    public MaskingSetting() { }

    /**
     * Construct {@code MaskingSetting} with specified masking setting.
     * @param charactersMaskLeft Number of characters to mask from left.
     * @param charactersMaskRight Number of characters to mask from right.
     * @param percentMaskLeft Percent of characters to mask from left.
     * @param percentMaskRight Percent of characters to mask from right.
     * @param maskingCharacter Masking character to use (default is 'X');
     */
    public MaskingSetting(int charactersMaskLeft, int charactersMaskRight,
                          double percentMaskLeft, double percentMaskRight,
                          char maskingCharacter) {
        this.charactersMaskLeft = charactersMaskLeft;
        this.charactersMaskRight = charactersMaskRight;
        this.percentMaskLeft = percentMaskLeft;
        this.percentMaskRight = percentMaskRight;
        this.maskingCharacter = maskingCharacter;
    }

    /**
     * A helper method that returns the number or characters to mask from left based
     * on masking settings (combined).
     * @param sb {@code StringBuffer} that contains the value.
     * @return Number of characters to mask from left.
     */
    public int getNumCharactersToMaskLeft(StringBuffer sb) {
        if (sb == null) {
            return 0;
        }

        return getNumCharactersToMaskLeft(sb.length());
    }

    /**
     * A helper method that returns the number or characters to mask from left based
     * on masking settings (combined).
     * @param sb {@code StringBuilder} that contains the value.
     * @return Number of characters to mask from left.
     */
    public int getNumCharactersToMaskLeft(StringBuilder sb) {
            if (sb == null) {
                return 0;
            }

            return getNumCharactersToMaskLeft(sb.length());
    }

    /**
     * A helper method that returns the number or characters to mask from left based
     * on masking settings (combined).
     * @param str {@code String} that contains the value.
     * @return Number of characters to mask from left.
     */
    public int getNumCharactersToMaskLeft(String str) {
        return getNumCharactersToMaskLeft(str.length());
    }


    /**
     * A helper method that returns the number or characters to mask from left based
     * on masking settings (combined).
     * @param strLength {@code int} that contains the number of characters.
     * @return Number of characters to mask from left.
     */
    public int getNumCharactersToMaskLeft(int strLength) {
        if (charactersMaskLeft == 0 && percentMaskLeft == 0) {
            return 0;
        }
        if (charactersMaskLeft > (percentMaskLeft / 100) * strLength) {
            return Math.min(charactersMaskLeft, strLength);
        } else {
            return Math.min((int) Math.ceil((percentMaskLeft / 100) * strLength), strLength);
        }
    }

    /**
     * A helper method that returns the number or characters to mask from right based
     * on masking settings (combined).
     * @param sb {@code StringBuffer} that contains the value.
     * @return Number of characters to mask from right.
     */
    public int getNumCharactersToMaskRight(StringBuffer sb) {
        if (sb == null) {
            return 0;
        }

        return getNumCharactersToMaskRight(sb.length());
    }

    /**
     * A helper method that returns the number or characters to mask from right based
     * on masking settings (combined).
     * @param sb {@code StringBuilder} that contains the value.
     * @return Number of characters to mask from right.
     */
    public int getNumCharactersToMaskRight(StringBuilder sb) {
        if (sb == null) {
            return 0;
        }

        return getNumCharactersToMaskRight(sb.length());
    }

    /**
     * A helper method that returns the number or characters to mask from right based
     * on masking settings (combined).
     * @param str {@code String} that contains the value.
     * @return Number of characters to mask from right.
     */
    public int getNumCharactersToMaskRight(String str) {
        return getNumCharactersToMaskRight(str.length());
    }

    /**
     * A helper method that returns the number or characters to mask from right based
     * on masking settings (combined).
     * @param strLength {@code int} that contains the value of number of characters.
     * @return Number of characters to mask from right.
     */
    public int getNumCharactersToMaskRight(int strLength) {
        if (charactersMaskRight == 0 && percentMaskRight == 0) {
            return 0;
        }
        if (charactersMaskRight > (percentMaskRight / 100) * strLength) {
            return Math.min(charactersMaskRight, strLength);
        } else {
            return Math.min((int) Math.ceil((percentMaskRight / 100) * strLength), strLength);
        }
    }

    /**
     * Returns the number of characters to mask from left.
     * @return Number of characters to mask from left.
     */
    public int getCharactersMaskLeft() {
        return charactersMaskLeft;
    }

    /**
     * Set number of characters to mask from left.
     * @param charactersMaskLeft Set number of characters to mask from left.
     */
    public void setCharactersMaskLeft(int charactersMaskLeft) {
        this.charactersMaskLeft = charactersMaskLeft;
    }

    /**
     * Returns number of characters to mask from right.
     * @return Number of characters to mask from right.
     */
    public int getCharactersMaskRight() {
        return charactersMaskRight;
    }

    /**
     * Sets number of characters to mask from left.
     * @param charactersMaskRight Set number of characters to mask from left.
     */
    public void setCharactersMaskRight(int charactersMaskRight) {
        this.charactersMaskRight = charactersMaskRight;
    }

    /**
     * Return the value of percent of characters to mask from left.
     * @return Get value of percent of characters to mask from left.
     */
    public double getPercentMaskLeft() {
        return percentMaskLeft;
    }

    /**
     * Set the value for percent of characters to mask from left.
     * @param percentMaskLeft Set value for percent of characters to mask from left.
     */
    public void setPercentMaskLeft(double percentMaskLeft) {
        this.percentMaskLeft = percentMaskLeft;
    }

    /**
     * Return the value of percent of characters to mask from right.
     * @return Get value of percent of characters to mask from right.
     */
    public double getPercentMaskRight() {
        return percentMaskRight;
    }

    /**
     * Set the value for percent of characters to mask from right.
     * @param percentMaskRight Set value for percent of characters to mask from right.
     */
    public void setPercentMaskRight(double percentMaskRight) {
        this.percentMaskRight = percentMaskRight;
    }

    /**
     * Return the current masking character.
     * @return Current masking character.
     */
    public char getMaskingCharacter() {
        return maskingCharacter;
    }

    /**
     * Set the masking character.
     * @param maskingCharacter Set masking character to use.
     */
    public void setMaskingCharacter(char maskingCharacter) {
        this.maskingCharacter = maskingCharacter;
    }

    /**
     * Method is used to apply the masking setting to input string and produce the masked string.
     * @param str {@code String} to mask.
     * @return Masked string.
     */
    public String apply(String str) {
        if (str == null || str.length() == 0){
           return str;
        }
        StringBuffer sb = new StringBuffer(str);
        apply(sb);
        return sb.toString();
    }

    /**
     * Method is used to apply the masking setting to {@code StringBuilder}.
     * The {@code StringBuilder} is modified.
     * @param sb {@code StringBuilder} to mask.
     */
    public void apply(StringBuilder sb) {
        if (sb == null || sb.length() == 0) {
            return;
        }

        int leftMask = getNumCharactersToMaskLeft(sb);
        int rightMask = getNumCharactersToMaskRight(sb);

        if (sb.length() <= leftMask || sb.length() <= rightMask) {
            for (int i = 0; i < sb.length(); i++) {
                sb.setCharAt(i, maskingCharacter);
            }
            return;
        }

        if (leftMask > 0) {
            for (int i = 0; i < leftMask; i++) {
                sb.setCharAt(i, maskingCharacter);
            }
        }

        if (rightMask > 0) {
            for (int i = sb.length() - rightMask; i < sb.length(); i++) {
                sb.setCharAt(i, maskingCharacter);
            }
        }

        if (!innerMasks.isEmpty()) {
            for (int[] setting : innerMasks) {
                for (int idx = setting[0] - 1; idx < setting[1]; idx++) {
                    if (idx >= sb.length()) {
                        break;
                    }
                    sb.setCharAt(idx, maskingCharacter);
                }
            }
        }
    }

    /**
     * Method is used to apply the masking setting to {@code StringBuffer}.
     * The {@code StringBuffer} is modified.
     * @param sb {@code StringBuffer} to mask.
     */
    public void apply(StringBuffer sb) {
        if (sb == null || sb.length() == 0) {
            return;
        }

        int leftMask = getNumCharactersToMaskLeft(sb);
        int rightMask = getNumCharactersToMaskRight(sb);

        if (sb.length() <= leftMask || sb.length() <= rightMask) {
            for (int i = 0; i < sb.length(); i++) {
                sb.setCharAt(i, maskingCharacter);
            }
            return;
        }

        if (leftMask > 0) {
            for (int i = 0; i < leftMask; i++) {
                sb.setCharAt(i, maskingCharacter);
            }
        }

        if (rightMask > 0) {
            for (int i = sb.length() - rightMask; i < sb.length(); i++) {
                sb.setCharAt(i, maskingCharacter);
            }
        }

        if (!innerMasks.isEmpty()) {
            for (int[] setting : innerMasks) {
                for (int idx = setting[0] - 1; idx < setting[1]; idx++) {
                    if (idx >= sb.length()) {
                        break;
                    }
                    sb.setCharAt(idx, maskingCharacter);
                }
            }
        }
    }

    /**
     * Method is used to add inner masking ranges to masking settings.  This method
     * may be called multiple times to add additional inner masking settings.
     * @param start Start index (zero-based) of inner masking range.
     * @param end End index (zero-based) of inner masking range.
     */
    public void addInnerMasking(int start, int end) {
        innerMasks.add(new int[]{start, end});
    }

}
