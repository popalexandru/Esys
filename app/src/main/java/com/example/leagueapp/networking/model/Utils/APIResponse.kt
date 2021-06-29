package com.example.leagueapp.networking.model.Utils

enum class APIResponse(val code : Int){
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    UNSUPPORTED(415),
    RATE_LIMIT_EXCEEDED(429),
    OK(200),
    INTERNAL_SERVER_ERROR(500),
    SERVER_INNACESSIBLE(502),
    SERVICE_UNAVAILABLE(503)
}