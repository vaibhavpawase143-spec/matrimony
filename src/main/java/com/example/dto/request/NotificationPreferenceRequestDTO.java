package com.example.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationPreferenceRequestDTO {

    private Boolean matchNotifications;

    private Boolean interestNotifications;

    private Boolean messageNotifications;

    private Boolean profileViewNotifications;

    private Boolean promotionalEmails;

}