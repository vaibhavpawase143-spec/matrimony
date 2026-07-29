CREATE TABLE notification_preferences (

                                          id BIGSERIAL PRIMARY KEY,

                                          user_id BIGINT NOT NULL UNIQUE,

                                          match_notifications BOOLEAN NOT NULL DEFAULT TRUE,

                                          interest_notifications BOOLEAN NOT NULL DEFAULT TRUE,

                                          message_notifications BOOLEAN NOT NULL DEFAULT TRUE,

                                          profile_view_notifications BOOLEAN NOT NULL DEFAULT FALSE,

                                          promotional_emails BOOLEAN NOT NULL DEFAULT FALSE,

                                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                          CONSTRAINT fk_notification_preferences_user
                                              FOREIGN KEY (user_id)
                                                  REFERENCES users(id)
                                                  ON DELETE CASCADE

);