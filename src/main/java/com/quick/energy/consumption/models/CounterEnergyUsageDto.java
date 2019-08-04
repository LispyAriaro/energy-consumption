package com.quick.energy.consumption.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * @author efe ariaroo
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CounterEnergyUsageDto {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "counter id is a required field")
    private String counterId;

    @Positive(message = "Amount is a required field")
    private BigDecimal amount;
}
