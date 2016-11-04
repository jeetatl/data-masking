package com.jeetatl.datamasking;

/**
 * Created by mkhokhar on 11/3/16.
 */


public class MaskingSetting {
    private static final int DEFAULT_CHARACTERS_MASK_LEFT = 0;
    private static final int DEFAULT_CHARACTERS_MASK_RIGHT = 0;
    private static final double DEFAULT_PERCENTAGE_MASK_LEFT = 0;
    private static final double DEFAULT_PERCENTAGE_MASK_RIGHT = 0;
    private static final char DEFAULT_MASKING_CHARACTER = 'X';
    private static final String DEFAULT_MASKING_STRING = "X";

    private int charactersMaskLeft = DEFAULT_CHARACTERS_MASK_LEFT;
    private int charactersMaskRight = DEFAULT_CHARACTERS_MASK_RIGHT;
    private double percentMaskLeft = DEFAULT_PERCENTAGE_MASK_LEFT;
    private double percentMaskRight = DEFAULT_PERCENTAGE_MASK_RIGHT;
    private char maskingCharacter = DEFAULT_MASKING_CHARACTER;
    private String maskingString = DEFAULT_MASKING_STRING;

    public MaskingSetting() {
    }

    public MaskingSetting(int charactersMaskLeft, int charactersMaskRight,
                          double percentMaskLeft, double percentMaskRight,
                          char maskingCharacter) {
        this.charactersMaskLeft = charactersMaskLeft;
        this.charactersMaskRight = charactersMaskRight;
        this.percentMaskLeft = percentMaskLeft;
        this.percentMaskRight = percentMaskRight;
        this.maskingCharacter = maskingCharacter;
        this.maskingString = "" + maskingCharacter;
    }

    public int getNumCharactersToMaskLeft(StringBuffer sb) {
        if (sb == null) {
            return 0;
        }

        return getNumCharactersToMaskLeft(sb.length());
    }

    public int getNumCharactersToMaskLeft(StringBuilder sb) {
            if (sb == null) {
                return 0;
            }

            return getNumCharactersToMaskLeft(sb.length());
    }

    public int getNumCharactersToMaskLeft(String str) {
        return getNumCharactersToMaskLeft(str.length());
    }

    public int getNumCharactersToMaskLeft(int strLength) {
        if (charactersMaskLeft == 0 && percentMaskLeft == 0) {
            return 0;
        }
        if (charactersMaskLeft > (percentMaskLeft / 100) * strLength) {
            return Math.min(charactersMaskLeft, strLength);
        } else {
            return Math.min((int) Math.round((percentMaskLeft / 100) * strLength), strLength);
        }
    }

    public int getNumCharactersToMaskRight(StringBuffer sb) {
        if (sb == null) {
            return 0;
        }

        return getNumCharactersToMaskRight(sb.length());
    }

    public int getNumCharactersToMaskRight(StringBuilder sb) {
        if (sb == null) {
            return 0;
        }

        return getNumCharactersToMaskRight(sb.length());
    }
    public int getNumCharactersToMaskRight(String str) {
        return getNumCharactersToMaskRight(str.length());
    }

    public int getNumCharactersToMaskRight(int strLength) {
        if (charactersMaskRight == 0 && percentMaskRight == 0) {
            return 0;
        }
        if (charactersMaskRight > (percentMaskRight / 100) * strLength) {
            return Math.min(charactersMaskRight, strLength);
        } else {
            return Math.min((int) Math.round((percentMaskRight / 100) * strLength), strLength);
        }
    }

    public int getCharactersMaskLeft() {
        return charactersMaskLeft;
    }

    public void setCharactersMaskLeft(int charactersMaskLeft) {
        this.charactersMaskLeft = charactersMaskLeft;
    }

    public int getCharactersMaskRight() {
        return charactersMaskRight;
    }

    public void setCharactersMaskRight(int charactersMaskRight) {
        this.charactersMaskRight = charactersMaskRight;
    }

    public double getPercentMaskLeft() {
        return percentMaskLeft;
    }

    public void setPercentMaskLeft(double percentMaskLeft) {
        this.percentMaskLeft = percentMaskLeft;
    }

    public double getPercentMaskRight() {
        return percentMaskRight;
    }

    public void setPercentMaskRight(double percentMaskRight) {
        this.percentMaskRight = percentMaskRight;
    }

    public char getMaskingCharacter() {
        return maskingCharacter;
    }

    public void setMaskingCharacter(char maskingCharacter) {
        this.maskingCharacter = maskingCharacter;
        this.maskingString = "" + maskingCharacter;
    }

    public String getMaskingString() {
        return maskingString;
    }

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
    }

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
    }

    @Override
    public String toString() {
        return "MaskingSetting{" +
                "charactersMaskLeft=" + charactersMaskLeft +
                ", charactersMaskRight=" + charactersMaskRight +
                ", percentMaskLeft=" + percentMaskLeft +
                ", percentMaskRight=" + percentMaskRight +
                ", maskingCharacter=" + maskingCharacter +
                '}';
    }
}