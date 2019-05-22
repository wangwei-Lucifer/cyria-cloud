package com.kunteng.cyria.dashboard.domain;

import lombok.Data;

@Data
public class UserLoginDTO {
	private JWT jwt;
    private User user;
    private String xxx;
}
