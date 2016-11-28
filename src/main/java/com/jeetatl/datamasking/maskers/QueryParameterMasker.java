package com.jeetatl.datamasking.maskers;

import com.jeetatl.datamasking.config.MaskingConfiguration;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * <p>{@code QueryParameterMasker} is a concrete class used to apply {@link MaskingConfiguration} to
 * query parameters.</p>
 *
 * The masking is applied to any query parameter value that is currently configured within the
 * {@link MaskingConfiguration}.
 */
public class QueryParameterMasker {

    MaskingConfiguration config = null;

    /**
     *  Constructs a {@code QueryParameterMasker} with the provided configuration settings.
     * @param config Configuration settings to use with this {@code QueryParameterMasker}.
     */
    public QueryParameterMasker(MaskingConfiguration config) {
        this.config = config;
    }

    /**
     * This method is used to apply the masking settings to the query parameters.
     * @param payload A string containing valid query parameters.
     * @return A string containing query parameter payload after the masking settings have been applied.
     */
    public String getMasked(String payload) {
        if (payload == null || payload.isEmpty()) {
            return payload;
        }

        StringBuilder sb = new StringBuilder();

        try {
            String[] tokens = payload.split("&");
            for (int i = 0; i < tokens.length; i++) {
                String[] paramAndValue = tokens[i].split("=");
                if (paramAndValue.length == 2) {
                    sb.append(paramAndValue[0] + "=" + URLEncoder.encode(config.apply(URLDecoder.decode(paramAndValue[0], "UTF-8"), URLDecoder.decode(paramAndValue[1], "UTF-8")), "UTF-8"));
                }
                if (i != tokens.length - 1) {
                    sb.append("&");
                }
            }
        } catch (Exception e) {
            return payload;
        }

        return sb.toString();
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
