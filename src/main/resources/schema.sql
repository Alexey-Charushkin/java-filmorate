CREATE TABLE IF NOT EXISTS `users` (
                         `user_id` int PRIMARY KEY AUTO_INCREMENT,
                         `email` varchar(256),
                         `login` varchar(50),
                         `user_name` varchar(50),
                         `birthdate` date
);

CREATE TABLE IF NOT EXISTS `users_friends_id` (
                                    `users_friends_id` int PRIMARY KEY AUTO_INCREMENT,
                                    `user_id` int,
                                    `friend_id` int,
                                    `friend_status` varchar(11)
);

CREATE TABLE IF NOT EXISTS `films` (
                         `film_id` int PRIMARY KEY AUTO_INCREMENT,
                         `film_name` varchar(50),
                         `description` varchar(200),
                         `release_date` date,
                         `duration` int,
                         `mpa_rating_id` int,
                         `rate` int
);

CREATE TABLE IF NOT EXISTS `users_films_like` (
                                    `users_films_like_id` int PRIMARY KEY AUTO_INCREMENT,
                                    `user_id` int,
                                    `film_id` int
);

CREATE TABLE IF NOT EXISTS `genre` (
                         `genre_id` int PRIMARY KEY AUTO_INCREMENT,
                         `genre_name` varchar(50)
);

CREATE TABLE IF NOT EXISTS `films_genres` (
                                `films_genres_id` int PRIMARY KEY AUTO_INCREMENT,
                                `film_id` int,
                                `genre_id` int
);

CREATE TABLE IF NOT EXISTS `mpa_rating` (
                              `id` int PRIMARY KEY UNIQUE,
                              `mpa_rating_name` varchar(5)
);

ALTER TABLE `films_genres` ADD FOREIGN KEY (`film_id`) REFERENCES `films` (`film_id`) ON DELETE CASCADE;

ALTER TABLE `users_films_like` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;

ALTER TABLE `users_films_like` ADD FOREIGN KEY (`film_id`) REFERENCES `films` (`film_id`) ON DELETE CASCADE;

ALTER TABLE `films_genres` ADD FOREIGN KEY (`genre_id`) REFERENCES `genre` (`genre_id`) ON DELETE CASCADE;

ALTER TABLE `users_friends_id` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;

ALTER TABLE `users_friends_id` ADD FOREIGN KEY (`friend_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;

ALTER TABLE `films` ADD FOREIGN KEY (`mpa_rating_id`) REFERENCES `mpa_rating` (`id`);


