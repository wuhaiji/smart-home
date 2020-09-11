package com.ecoeler.app.dto.v1;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@RequiredArgsConstructor(staticName = "of")
public class UserDto {
    /**
     * 用户ID
     */
    Long userId;
}
