package com.ifelseelif.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Address {
    private String zipCode;
    private Location town;
}
