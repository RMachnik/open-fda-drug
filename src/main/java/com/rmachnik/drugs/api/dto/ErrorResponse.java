package com.rmachnik.drugs.api.dto;

import java.util.Map;


public record ErrorResponse(Map<String, String> error) {
}
