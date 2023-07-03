package com.example.convenientshoppingapp.dto.food;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
public class CreateFoodHistoryRequest {
    @NotNull(message = "Mã thực phẩm không được để trống")
    private Long foodId;

    @NotNull(message = "Số lượng không được để trống")
    private Double quantity;

    @NotNull(message = "Mã đơn vị thực phẩm không được để trống")
    private Long measureId;
}
