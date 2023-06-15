package com.example.convenientshoppingapp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
    /*
    Class này dùng để trả về kết quả cho client biết được trang thái của request là gì.
     */
public class ResponseObject {


    private String status;
    private String message;
    private Object data;
}
