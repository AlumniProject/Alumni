package Alumni.backend.module.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ListResponse<T> {
    private int count;
    private Integer code;
    private String message;
    private T data;
}