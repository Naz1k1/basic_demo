package com.naz1k1.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_roles")
public class UserRole {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;
    private Integer roleId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
