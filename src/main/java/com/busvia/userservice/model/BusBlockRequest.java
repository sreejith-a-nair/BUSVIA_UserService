package com.busvia.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusBlockRequest {
    private UUID uuid;
    private boolean isAvailable;
}
