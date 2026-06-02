package com.example.config;

import com.example.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@RequiredArgsConstructor
public class WebSocketAuthInterceptor
        implements HandshakeInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(

            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String,Object> attributes

    ){

        try{

            // GET TOKEN FROM URL
            String token =
                    request.getURI()
                            .getQuery();

            if(

                    token==null ||

                            !token.startsWith(
                                    "token="
                            )

            ){

                System.out.println(
                        "❌ Missing Token"
                );

                return false;

            }

            token =
                    token.replace(
                            "token=",
                            ""
                    );
            System.out.println(

                    "TOKEN RECEIVED: " +

                            token

            );
            String username =
                    jwtUtil.extractUsername(
                            token
                    );

            if(

                    !jwtUtil.isValid(
                            token,
                            username
                    )

            ){

                System.out.println(
                        "❌ Invalid JWT"
                );

                return false;

            }

            attributes.put(

                    "username",

                    username

            );

            System.out.println(

                    "✅ WebSocket Connected: "

                            + username

            );

            return true;

        }

        catch(Exception e){

            System.out.println(

                    "❌ WebSocket Error: "

                            + e.getMessage()

            );

            return false;

        }

    }

    @Override
    public void afterHandshake(

            ServerHttpRequest request,

            ServerHttpResponse response,

            WebSocketHandler wsHandler,

            Exception exception

    ){

    }

}