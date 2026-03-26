package com.example.parking.dto;

import javax.validation.constraints.NotNull;

public class EnabledRequest {
    @NotNull
    private Boolean enabled;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}

