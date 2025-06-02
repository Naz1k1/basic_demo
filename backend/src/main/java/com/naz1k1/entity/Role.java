package com.naz1k1.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("roles")
public class Role {
    @TableId(type = IdType.AUTO)
    private Integer roleId;

    private String roleName;
    private String description;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
