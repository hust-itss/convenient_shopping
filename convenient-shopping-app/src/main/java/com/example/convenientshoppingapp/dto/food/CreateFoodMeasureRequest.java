package com.example.convenientshoppingapp.dto.food;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
@Setter
@Getter
public class CreateFoodMeasureRequest {
    @NotEmpty(message = "Tên đơn vị không được để trống")
    @Length(min = 2, max = 64, message = "Tên đơn vị có độ dài từ 2 đến 64 ký tự")
    private String name;
}
