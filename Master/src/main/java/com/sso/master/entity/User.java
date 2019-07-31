package com.sso.master.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author:xjk 2019/7/31 18:10
 * com.sso.master.entity
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 7591962588453679424L;

    private Long id;

    private String userName;

    private String password;

    private String salt;
}
