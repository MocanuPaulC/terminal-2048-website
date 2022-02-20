DROP table if exists INT_squares;
drop table if exists INT_boards;
drop table if exists INT_players;
drop table if exists squares;
DROP TABLE IF EXISTS boards;
Drop table if exists players;
DROP SEQUENCE IF EXISTS seq_player_id;
DROP SEQUENCE IF EXISTS seq_board_id;
CREATE TABLE IF NOT EXISTS INT_players
(
    playerid   numeric(3)
        constraint pk_player PRIMARY KEY,
    name VARCHAR(50)
        constraint ch_name_length check ( LENGTH(name) <= 50 )

);


CREATE TABLE IF NOT EXISTS INT_Boards
(
    playerid    numeric(3)
        constraint fk_board_player references INT_players (playerid),
    boardid numeric(3)
        constraint pk_boards PRIMARY KEY,
    score numeric(4)
        constraint ch_score check ( score >= 0 )
);


CREATE TABLE IF NOT EXISTS INT_squares
(int_boards
    squareid numeric(2),
    boardid numeric(3)
        constraint fk_boards references INT_boards(boardid),
    value numeric(4),
    constraint pk_squares PRIMARY KEY(squareid, boardid)
);

CREATE SEQUENCE IF NOT EXISTS seq_player_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS seq_board_id START WITH 1 INCREMENT BY 1;
