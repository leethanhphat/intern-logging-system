package vn.vifo.logging.config;

import vn.vifo.logging.service.websocket.WebSocketInterceptor;
import vn.vifo.logging.service.websocket.WebsocketChannelHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebsocketConfig implements WebSocketConfigurer {
    private final WebsocketChannelHandler websocketChannelHandler;

    private final WebSocketInterceptor webSocketInterceptor;

    /**
     * Register websocket handlers.
     * @param registry WebSocketHandlerRegistry
     */
    @Override
    public void registerWebSocketHandlers(final WebSocketHandlerRegistry registry) {
        registry
                .addHandler(websocketChannelHandler, "/ws/{token}")
                .addInterceptors(webSocketInterceptor)
                .setAllowedOrigins("*");
    }
}