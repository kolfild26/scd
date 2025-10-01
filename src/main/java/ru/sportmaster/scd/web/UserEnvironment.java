package ru.sportmaster.scd.web;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEnvironment {
    private Long userId;
    private String requestUuid;
    private String osUser;
    private String host;
    private String ipAddress;
    private String module;
}
