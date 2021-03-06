package jmri.server.json.signalMast;

import static jmri.server.json.JSON.ASPECT;
import static jmri.server.json.JSON.ASPECT_DARK;
import static jmri.server.json.JSON.ASPECT_HELD;
import static jmri.server.json.JSON.ASPECT_UNKNOWN;
import static jmri.server.json.JSON.DATA;
import static jmri.server.json.JSON.LIT;
import static jmri.server.json.JSON.STATE;
import static jmri.server.json.JSON.TOKEN_HELD;
import static jmri.server.json.JSON.TYPE;
import static jmri.server.json.signalMast.JsonSignalMast.SIGNAL_MAST;
import static jmri.server.json.signalMast.JsonSignalMast.SIGNAL_MASTS;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Locale;
import javax.servlet.http.HttpServletResponse;
import jmri.InstanceManager;
import jmri.SignalMast;
import jmri.SignalMastManager;
import jmri.server.json.JsonException;
import jmri.server.json.JsonNamedBeanHttpService;

/**
 * JSON HTTP service for {@link jmri.SignalMast}s.
 *
 * @author Randall Wood Copyright 2016, 2018
 */
public class JsonSignalMastHttpService extends JsonNamedBeanHttpService {

    public JsonSignalMastHttpService(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public JsonNode doGet(String type, String name, Locale locale) throws JsonException {
        SignalMast signalMast = InstanceManager.getDefault(jmri.SignalMastManager.class).getSignalMast(name);
        ObjectNode root = this.getNamedBean(signalMast, name, type, locale);
        ObjectNode data = root.with(DATA);
        if (signalMast != null) {
            String aspect = signalMast.getAspect();
            if (aspect == null) {
                aspect = ASPECT_UNKNOWN; //if null, set aspect to "Unknown"
            }
            data.put(ASPECT, aspect);
            data.put(LIT, signalMast.getLit());
            data.put(TOKEN_HELD, signalMast.getHeld());
            //state is appearance, plus flags for held and dark statuses
            if ((signalMast.getHeld()) && (signalMast.getAppearanceMap().getSpecificAppearance(jmri.SignalAppearanceMap.HELD) != null)) {
                data.put(STATE, ASPECT_HELD);
            } else if ((!signalMast.getLit()) && (signalMast.getAppearanceMap().getSpecificAppearance(jmri.SignalAppearanceMap.DARK) != null)) {
                data.put(STATE, ASPECT_DARK);
            } else {
                data.put(STATE, aspect);
            }
        }
        return root;
    }

    @Override
    public JsonNode doPost(String type, String name, JsonNode data, Locale locale) throws JsonException {
        SignalMast signalMast = InstanceManager.getDefault(jmri.SignalMastManager.class).getSignalMast(name);
        this.postNamedBean(signalMast, data, name, type, locale);
        if (signalMast != null) {
            if (data.path(STATE).isTextual()) {
                String aspect = data.path(STATE).asText();
                if (aspect.equals("Held")) {
                    signalMast.setHeld(true);
                } else if (signalMast.getValidAspects().contains(aspect)) {
                    if (signalMast.getHeld()) {
                        signalMast.setHeld(false);
                    }
                    if (signalMast.getAspect() == null || !signalMast.getAspect().equals(aspect)) {
                        signalMast.setAspect(aspect);
                    }
                } else {
                    throw new JsonException(400, Bundle.getMessage(locale, "ErrorUnknownState", SIGNAL_MAST, aspect));
                }
            }
        }
        return this.doGet(type, name, locale);
    }

    @Override
    public ArrayNode doGetList(String type, Locale locale) throws JsonException {
        ArrayNode root = this.mapper.createArrayNode();

        for (String name : InstanceManager.getDefault(SignalMastManager.class
        ).getSystemNameList()) {
            root.add(this.doGet(SIGNAL_MAST, name, locale));
        }
        return root;
    }

    @Override
    public JsonNode doSchema(String type, boolean server, Locale locale) throws JsonException {
        switch (type) {
            case SIGNAL_MAST:
            case SIGNAL_MASTS:
                return doSchema(type,
                        server,
                        "jmri/server/json/signalMast/signalMast-server.json",
                        "jmri/server/json/signalMast/signalMast-client.json");
            default:
                throw new JsonException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Bundle.getMessage(locale, "ErrorUnknownType", type));
        }
    }
}
