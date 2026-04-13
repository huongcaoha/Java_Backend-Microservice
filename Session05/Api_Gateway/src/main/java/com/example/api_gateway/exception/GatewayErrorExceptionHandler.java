//package com.example.api_gateway.exception;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
//import org.springframework.core.annotation.Order;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ResponseStatusException;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//@Order(-1)
//public class GatewayErrorExceptionHandler implements ErrorWebExceptionHandler {
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Override
//    public Mono handle(ServerWebExchange exchange, Throwable ex) {
//        var response = exchange.getResponse();
//        if (response.isCommitted()) {
//            return Mono.error(ex);
//        }
//
//        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
//        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
//        String message = "Lỗi hệ thống Gateway không xác định";
//
//        if (ex instanceof ResponseStatusException) {
//            ResponseStatusException exception = (ResponseStatusException) ex;
//            status = (HttpStatus) exception.getStatusCode();
//            if (status == HttpStatus.SERVICE_UNAVAILABLE) {
//                message = "Cổng Gateway không thể kết nối tới dịch vụ đích. Dịch vụ có thể đang bị sập.";
//            } else if (status == HttpStatus.NOT_FOUND) {
//                message = "Đường dẫn API không tồn tại trên hệ thống.";
//            }
//        } else if (ex instanceof java.net.ConnectException || ex.getMessage().contains("Connection refused")) {
//            status = HttpStatus.SERVICE_UNAVAILABLE;
//            message = "Cổng Gateway không thể kết nối tới dịch vụ đích.";
//        }
//
//        response.setStatusCode(status);
//
//        Map<String, Object> errorAttributes = new HashMap<>();
//        errorAttributes.put("timestamp", LocalDateTime.now().toString());
//        errorAttributes.put("status", status.value());
//        errorAttributes.put("error", status.getReasonPhrase());
//        errorAttributes.put("message", message);
//
//        try {
//            byte[] bytes = objectMapper.writeValueAsBytes(errorAttributes);
//            DataBuffer buffer = response.bufferFactory().wrap(bytes);
//            return response.writeWith(Mono.just(buffer));
//        } catch (JsonProcessingException e) {
//            return Mono.error(e);
//        }
//    }
//}
