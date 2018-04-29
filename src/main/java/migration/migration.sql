-- A script to create raw_data table
create table raw_data(
    -- auto-generated primary key

    id int(11) UNSIGNED not null auto_increment,

    questionType text not null,

    asin text not null,

    answerTime  text not null,

    unixTime  bigint not null,

    question text not null,

    answerType text,

    answer text not null,

    PRIMARY KEY(id)

);

-- A script to create tokenized_data table
create table tokenized_data(
    -- auto-generated primary key

    id int(11) UNSIGNED not null auto_increment,

    question text not null,

    answer text not null,

    tokens  text not null,

    prob text not null,

    PRIMARY KEY(id)
);


create table adv_tokenized_data(

    id int(11) UNSIGNED NOT NULL auto_increment,

    question text NOT NULL,

    answer text NOT NULL,

    lemma text NOT NULL,

    stem text NOT NULL,

    pos text NOT NULL,

    tree text NOT NULL,

    wordnet_features text NOT NULL,

    PRIMARY KEY(id)
);