package com.jeetatl.datamasking.maskers;

import com.jeetatl.datamasking.config.MaskingConfiguration;
import org.junit.Assert;
import org.junit.Test;

public class QueryParameterMaskerTests {

    @Test
    public void testConstructor() {
        MaskingConfiguration config = new MaskingConfiguration("apple:~3");
        QueryParameterMasker masker = new QueryParameterMasker(config);
        Assert.assertEquals(config, masker.getConfig());
    }

    @Test
    public void testConfigSetter() {
        MaskingConfiguration config = new MaskingConfiguration("apple:~3");
        QueryParameterMasker masker = new QueryParameterMasker(config);

        MaskingConfiguration config2 = new MaskingConfiguration("orange:~5");
        masker.setConfig(config2);

        Assert.assertEquals(config2, masker.getConfig());
    }

    @Test
    public void testSimpleQueryMasking() {
        MaskingConfiguration config = new MaskingConfiguration("field1:+100%");
        QueryParameterMasker masker = new QueryParameterMasker(config);

        String stringToMask = "field1=queryparam1value";
        String maskedField = "field1=XXXXXXXXXXXXXXXX";

        Assert.assertEquals(maskedField, masker.getMasked(stringToMask));
    }

    @Test
    public void testMultipleSimpleQueryMasking() {
        MaskingConfiguration config = new MaskingConfiguration("field1:+100%|field2:-2");
        QueryParameterMasker masker = new QueryParameterMasker(config);

        String stringToMask = "field1=queryparam1value&field2=field2value";
        String maskedField = "field1=XXXXXXXXXXXXXXXX&field2=field2valXX";

        Assert.assertEquals(maskedField, masker.getMasked(stringToMask));
    }

    @Test
    public void testComplexQueryMasking() {
        MaskingConfiguration config = new MaskingConfiguration("field1:+100%|field2:-2");
        QueryParameterMasker masker = new QueryParameterMasker(config);

        String stringToMask = "field1=queryparam%201value&field2=field2value";
        String maskedField = "field1=XXXXXXXXXXXXXXXXX&field2=field2valXX";

        Assert.assertEquals(maskedField, masker.getMasked(stringToMask));
    }

    @Test
    public void testComplexQuery2Masking() {
        MaskingConfiguration config = new MaskingConfiguration("field1:+3|field2:-2");
        QueryParameterMasker masker = new QueryParameterMasker(config);

        String stringToMask = "field1=queryparam+1value&field2=field2value%25%26";
        String maskedField = "field1=XXXryparam+1value&field2=field2valueXX";

        Assert.assertEquals(maskedField, masker.getMasked(stringToMask));
    }

    @Test
    public void testBadQueryMasking() {
        MaskingConfiguration config = new MaskingConfiguration("field1:+3|field2:-2");
        QueryParameterMasker masker = new QueryParameterMasker(config);

        String stringToMask = "field1=%z9";
        String maskedField = stringToMask;

        Assert.assertEquals(maskedField, masker.getMasked(stringToMask));
    }

    @Test
    public void testNullQueryMasking() {
        MaskingConfiguration config = new MaskingConfiguration("field1:+3|field2:-2");
        QueryParameterMasker masker = new QueryParameterMasker(config);

        String stringToMask = null;
        String maskedField = null;

        Assert.assertEquals(maskedField, masker.getMasked(stringToMask));
    }

    @Test
    public void testEmptyQueryMasking() {
        MaskingConfiguration config = new MaskingConfiguration("field1:+3|field2:-2");
        QueryParameterMasker masker = new QueryParameterMasker(config);

        String stringToMask = "";
        String maskedField = "";

        Assert.assertEquals(maskedField, masker.getMasked(stringToMask));
    }
}
