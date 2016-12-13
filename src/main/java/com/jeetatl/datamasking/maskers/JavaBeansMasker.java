package com.jeetatl.datamasking.maskers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeetatl.datamasking.config.MaskingConfiguration;
import com.jeetatl.datamasking.json.JsonPayloadMasker;

/**
 * <p>{@code JavaBeansMasker} is a concrete class used to apply {@link MaskingConfiguration} to
 * JavaBeans fields.</p>
 *
 * The masking is applied to any JavaBean field value that is currently configured within the
 * {@link MaskingConfiguration}.
 */
public class JavaBeansMasker {

    MaskingConfiguration config = null;

    /**
     *  Constructs a {@code JavaBeansMasker} with the provided configuration settings.
     * @param config Configuration settings to use with this {@code JavaBeansMasker}.
     */
    public JavaBeansMasker(MaskingConfiguration config) {
        this.config = config;
    }

    /**
     * This method is used to apply the masking settings to the JavaBean fields.
     * @param bean The object who's fields will be masked.
     * @return A string containing query parameter payload after the masking settings have been applied.
     */
    public String getMasked(Object bean) {
        String masked = null;

        try {
            ObjectMapper om = new ObjectMapper();
            String jsonPayload = om.writeValueAsString(bean);
            JsonPayloadMasker masker = new JsonPayloadMasker(getConfig());
            masked = masker.getMasked(jsonPayload);
        } catch (JsonProcessingException e) { }

        return masked;
    }


    /**
     * @return The masking configuration
     */
    public MaskingConfiguration getConfig() {
        return config;
    }

    /**
     * Set the masking configuration.
     * @param config Masking configuration to set.
     */
    public void setConfig(MaskingConfiguration config) {
        this.config = config;
    }
}
