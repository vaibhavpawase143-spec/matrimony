package com.example.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationPreferenceResponseDTO {

    private Long userId;

    private Boolean matchNotifications;

    private Boolean interestNotifications;

    private Boolean messageNotifications;

    private Boolean profileViewNotifications;

    private Boolean promotionalEmails;

}