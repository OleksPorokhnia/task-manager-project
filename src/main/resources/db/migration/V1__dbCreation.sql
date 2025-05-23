CREATE TABLE app_user
(
    id       INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    nickname VARCHAR(255)                             NOT NULL,
    email    VARCHAR(225)                             NOT NULL,
    password VARCHAR(255)                             NOT NULL,
    role     VARCHAR(255),
    CONSTRAINT pk_app_user PRIMARY KEY (id)
);

CREATE TABLE project
(
    id    INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title VARCHAR(100)                             NOT NULL,
    CONSTRAINT pk_project PRIMARY KEY (id)
);

CREATE TABLE task
(
    id          INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    priority    INTEGER,
    status      VARCHAR(255),
    user_id     INTEGER,
    project_id  INTEGER,
    creator_id  INTEGER,
    created_at  TIMESTAMP WITHOUT TIME ZONE,
    updated_at  TIMESTAMP WITHOUT TIME ZONE,
    deadline    date,
    description VARCHAR(3000),
    title       VARCHAR(100),
    CONSTRAINT pk_task PRIMARY KEY (id)
);

CREATE TABLE task_group
(
    id         INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name       VARCHAR(255),
    project_id INTEGER,
    CONSTRAINT pk_task_group PRIMARY KEY (id)
);

CREATE TABLE user_project
(
    project_id INTEGER NOT NULL,
    user_id    INTEGER NOT NULL
);

ALTER TABLE task_group
    ADD CONSTRAINT FK_TASK_GROUP_ON_PROJECT FOREIGN KEY (project_id) REFERENCES project (id);

ALTER TABLE task
    ADD CONSTRAINT FK_TASK_ON_CREATOR FOREIGN KEY (creator_id) REFERENCES app_user (id);

ALTER TABLE task
    ADD CONSTRAINT FK_TASK_ON_PROJECT FOREIGN KEY (project_id) REFERENCES project (id);

ALTER TABLE task
    ADD CONSTRAINT FK_TASK_ON_USER FOREIGN KEY (user_id) REFERENCES app_user (id);

ALTER TABLE user_project
    ADD CONSTRAINT fk_usepro_on_project FOREIGN KEY (project_id) REFERENCES project (id);

ALTER TABLE user_project
    ADD CONSTRAINT fk_usepro_on_user FOREIGN KEY (user_id) REFERENCES app_user (id);