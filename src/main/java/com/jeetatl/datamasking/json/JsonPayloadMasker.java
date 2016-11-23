package com.jeetatl.datamasking.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeetatl.datamasking.config.MaskingConfiguration;

import java.util.Map;

/**
 * <p>{@code JsonPayloadMasker} is a concrete class used to apply {@link MaskingConfiguration} to
 * JSON payloads.</p>
 *
 * The masking is applied to any element name that is currently configured within the
 * {@link MaskingConfiguration}.
 */
public class JsonPayloadMasker {

    MaskingConfiguration config = null;

    /**
     *  Constructs a {@code JsonPayloadMasker} with the provided configuration settings.
     * @param config Configuration settings to use with this {@code JsonPayloadMasker}.
     */
    public JsonPayloadMasker(MaskingConfiguration config) {
        this.config = config;
    }

    /**
     * This method is used to apply the masking settings to the json payload.
     * @param jsonPayload A string containing valid json.
     * @return A string containing json payload after the masking settings have been applied.
     */
    public String getMasked(String jsonPayload) {
        if (jsonPayload == null || jsonPayload.isEmpty()) {
            return jsonPayload;
        }

        String maskedStr = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> payloadMap = mapper.readValue(jsonPayload, Map.class);
            maskMap(payloadMap);
            maskedStr = mapper.writeValueAsString(payloadMap);
        } catch (Exception e) {
            return jsonPayload;
        }

        return maskedStr;
    }

    /**
     * A helper method to recursively search for all elements and to apply masking settings.
     * @param map The map representation of json payload.
     */
    private void maskMap(Map<String, Object> map) {
        for (String key : map.keySet()) {
            if (map.get(key) instanceof Map) {
                maskMap((Map) map.get(key));
            } else {
                map.put(key, config.apply(key, "" + map.get(key)));
            }
        }
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
