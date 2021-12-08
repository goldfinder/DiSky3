package info.itsthesky.disky3.core.oauth;

import bell.oauth.discord.main.Response;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import info.itsthesky.disky3.DiSky;
import info.itsthesky.disky3.api.Utils;
import info.itsthesky.disky3.api.section.EffectSection;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Represent the webserver where we'll catch back the Discord's authorization code.
 */
public class OAuthServer extends Thread {

    public static OAuthServer lastInstance = null;
    private static boolean alreadyStarted = false;

    private HttpServer server;
    private final OAuthWrapper wrapper;
    private final EffectSection section;

    public OAuthServer(OAuthWrapper wrapper, EffectSection section) {
        if (lastInstance != null)
            lastInstance.shutdown();
        lastInstance = this;
        this.wrapper = wrapper;
        this.section = section;
        try {
            this.server = HttpServer.create(new InetSocketAddress(OAuthManager.getPort()), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!alreadyStarted)
        {
            DiSky.success("Starting webserver for oauth at: " + wrapper.getDomain());
            alreadyStarted = true;
        }
        init();
        start();
    }

    private void init() {
        server.createContext("/oauth/", exchange -> Utils.sync(() -> { // Keep loading in the bukkit's thread

            final StringBuilder content = new StringBuilder();
            Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
            final @Nullable String code = params.getOrDefault("code", null);
            if (code == null) {
                answer(OAuthManager.getErrorCode(), exchange);
                return;
            }

            final Response response = getWrapper().getInstance().exchange(code);

            final Event event = new WhenResponse.ResponseEvent(exchange, getWrapper(), response);
            getSection().runSection(event);

            answer(OAuthManager.getValidCode(), exchange);
        }));
    }

    private void answer(String code, HttpExchange exchange) {
        try {
            exchange.sendResponseHeaders(200, code.getBytes(StandardCharsets.UTF_8).length);
            OutputStream out = exchange.getResponseBody();
            out.write(code.getBytes(StandardCharsets.UTF_8));
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        server.start();
    }

    public void shutdown() {
        server.stop(0);
    }

    private Map<String, String> queryToMap(String query) {
        if (query == null) {
            return new HashMap<>();
        }
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            }else{
                result.put(entry[0], "");
            }
        }
        return result;
    }

    public String getIP() {
        String ip;
        try (final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
        } catch (Exception ex) {
            ip = null;
        }
        return ip;
    }

    public HttpServer getServer() {
        return server;
    }

    public OAuthWrapper getWrapper() {
        return wrapper;
    }

    public EffectSection getSection() {
        return section;
    }
}
