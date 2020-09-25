package com.ecoeler.action.alexa;

/**
 * @author whj
 * @createTime 2020-02-19 11:48
 * @description
 **/

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

@Data
@Slf4j
public class AlexaResponse {

    private JSONObject response = new JSONObject("{}");
    private JSONObject event = new JSONObject("{}");
    private JSONObject header = new JSONObject("{}");
    private JSONObject endpoint = new JSONObject("{}");
    private JSONObject payload = new JSONObject("{}");

    private String CheckValue(String value, String defaultValue) {

        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }

        return value;
    }

    public AlexaResponse() {
        this("Alexa", "Response", "INVALID", "INVALID", null);
    }

    public AlexaResponse(String namespace, String name) {
        this(namespace, name, "INVALID", "INVALID", null);
    }

    public AlexaResponse(String namespace, String name, String endpointId, String token, String correlationToken) {

        header.put("namespace", CheckValue(namespace, "Alexa"));
        header.put("name", CheckValue(name, "Response"));
        header.put("messageId", UUID.randomUUID().toString());
        header.put("payloadVersion", "3");

        if (correlationToken != null) {
            header.put("correlationToken", CheckValue(correlationToken, "INVALID"));
        }

        JSONObject scope = new JSONObject("{}");
        scope.put("type", "BearerToken");
        scope.put("token", CheckValue(token, "INVALID"));

        endpoint.put("scope", scope);
        endpoint.put("endpointId", CheckValue(endpointId, "INVALID"));

        event.put("header", header);
        event.put("endpoint", endpoint);
        event.put("payload", payload);

        response.put("event", event);
    }


    public void AddCookie(String key, String value) {
        JSONObject endpointObject = response.getJSONObject("event").getJSONObject("endpoint");
        JSONObject cookie;
        if (endpointObject.has("cookie")) {

            cookie = endpointObject.getJSONObject("cookie");
            cookie.put(key, value);

        } else {
            cookie = new JSONObject();
            cookie.put(key, value);
            endpointObject.put("cookie", cookie);
        }

    }

    public void AddPayloadEndpoint(String friendlyName, String endpointId, String capabilities) {

        JSONObject payload = response.getJSONObject("event").getJSONObject("payload");

        if (payload.has("endpoints")) {
            JSONArray endpoints = payload.getJSONArray("endpoints");
            endpoints.put(new JSONObject(CreatePayloadEndpoint(friendlyName, endpointId, capabilities, null)));
        } else {
            JSONArray endpoints = new JSONArray();
            endpoints.put(new JSONObject(CreatePayloadEndpoint(friendlyName, endpointId, capabilities, null)));
            payload.put("endpoints", endpoints);
        }
    }

    public void AddContextProperty(String namespace, String name, String value, int uncertaintyInMilliseconds) {
        JSONObject context;
        JSONArray properties;
        try {
            context = response.getJSONObject("context");
            properties = context.getJSONArray("properties");

        } catch (JSONException jse) {
            context = new JSONObject();
            properties = new JSONArray();
            context.put("properties", properties);
        }

        properties.put(new JSONObject(CreateContextProperty(namespace, name, value, uncertaintyInMilliseconds)));
        response.put("context", context);

    }


    public String CreateContextProperty(String namespace, String name, String value, int uncertaintyInMilliseconds) {

        JSONObject property = new JSONObject();
        property.put("namespace", namespace);
        property.put("name", name);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
        TimeZone tz = TimeZone.getTimeZone("UTC");
        sdf.setTimeZone(tz);
        String timeOfSample = sdf.format(System.currentTimeMillis());

        property.put("timeOfSample", timeOfSample);
        property.put("uncertaintyInMilliseconds", uncertaintyInMilliseconds);

        // Handle either a JSON Object or value
        try {
            property.put("value", new JSONObject(value));
        } catch (JSONException je) {
            property.put("value", value);
        }

        return property.toString();
    }

    public String CreatePayloadEndpoint(String friendlyName, String endpointId, String capabilities, String cookie) {
        JSONObject endpoint = new JSONObject();
        endpoint.put("capabilities", new JSONArray(capabilities));
        endpoint.put("description", "Sample Endpoint Description");
        JSONArray displayCategories = new JSONArray("[\"OTHER\"]");
        endpoint.put("displayCategories", displayCategories);
        endpoint.put("manufacturerName", "Sample Manufacturer");

        if (endpointId == null) {
            endpointId = "endpoint_" + 100000 + new Random().nextInt(900000);
        }
        endpoint.put("endpointId", endpointId);

        if (friendlyName == null) {
            friendlyName = "Sample Endpoint";
        }
        endpoint.put("friendlyName", friendlyName);

        if (cookie != null) {
            endpoint.put("cookie", new JSONObject(cookie));
        }

        return endpoint.toString();
    }

    public String CreatePayloadEndpointCapability(String type, String interfaceValue, String version, String properties) {

        JSONObject capability = new JSONObject();
        capability.put("type", type);
        capability.put("interface", interfaceValue);
        capability.put("version", version);

        if (properties != null) {
            capability.put("properties", new JSONObject(properties));
        }
        return capability.toString();
    }

    public void setContext(String context) {
        response.put("context", new JSONObject(context));
    }

    public void SetPayload(String payload) {
        response.getJSONObject("event").put("payload", new JSONObject(payload));
    }

    public void setMessageId(String messageId) {
        response.getJSONObject("event").getJSONObject("header").put("messageId", messageId);
    }

    @Override
    public String toString() {
        return response.toString();
    }
}
