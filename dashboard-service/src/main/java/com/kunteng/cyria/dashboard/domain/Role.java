package com.kunteng.cyria.dashboard.domain;

import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;

public class Role implements GrantedAuthority{
	@Id
    private String id;

    private String name;

    public String getId() {
            return id;
    }

    public void setId(String id) {
            this.id = id;
    }

    public String getAuthority() {
            return name;
    }

    public void setName(String name) {
            this.name = name;
    }

    public String toString() {
            return name;
    }
}
