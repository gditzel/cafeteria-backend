package com.gonzalo.cafeteriabackend.application.port.in.product;

public interface ToggleProductStatus {
    ToggleProductStatusResponse execute(ToggleProductStatusRequest request);

    record ToggleProductStatusRequest(Long id) {}

    record ToggleProductStatusResponse(boolean toggled) {}
}
