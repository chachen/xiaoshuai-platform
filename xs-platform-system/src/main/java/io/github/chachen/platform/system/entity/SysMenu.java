package io.github.chachen.platform.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("xs_sys_menu")
public class SysMenu {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private Long parentId;
    private String name;
    private String path;
    private String permission;
    private Integer status;
    private Integer sort;

    public Long getId() { return id; }
    public void setId(Long v) { id = v; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long v) { parentId = v; }
    public String getName() { return name; }
    public void setName(String v) { name = v; }
    public String getPath() { return path; }
    public void setPath(String v) { path = v; }
    public String getPermission() { return permission; }
    public void setPermission(String v) { permission = v; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer v) { status = v; }
    public Integer getSort() { return sort; }
    public void setSort(Integer v) { sort = v; }
}
