package com.jeetatl.datamasking.maskers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeetatl.datamasking.config.MaskingConfiguration;
import org.junit.Assert;
import org.junit.Test;

public class JavaBeansMaskerTests {

    @Test
    public void testConstructor() {
        MaskingConfiguration config = new MaskingConfiguration("apple:~3");
        JavaBeansMasker masker = new JavaBeansMasker(config);
        Assert.assertEquals(config, masker.getConfig());
    }

    @Test
    public void testConfigSetter() {
        MaskingConfiguration config = new MaskingConfiguration("apple:~3");
        JavaBeansMasker masker = new JavaBeansMasker(config);

        MaskingConfiguration config2 = new MaskingConfiguration("orange:~5");
        masker.setConfig(config2);

        Assert.assertEquals(config2, masker.getConfig());
    }

    @Test
    public void testSimpleJavaBean() {
        class SimpleJavaBean {
            int i = 123456;
            String name = "ABCDEFG";

            public int getI() {
                return i;
            }

            public void setI(int i) {
                this.i = i;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        MaskingConfiguration config = new MaskingConfiguration("i:~3|name:+50%");
        JavaBeansMasker masker = new JavaBeansMasker(config);
        SimpleJavaBean bean = new SimpleJavaBean();
        String expectedMasked = "{\"i\":\"12X456\",\"name\":\"XXXXEFG\"}";
        Assert.assertEquals(expectedMasked, masker.getMasked(bean));
    }

}
