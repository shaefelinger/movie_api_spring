DELETE FROM movie;

INSERT INTO movie (id, title, overview, tagline, runtime, release_date, revenue, poster_path)
VALUES ('f74cf1ca-8c7b-435b-96c6-e4448a653596', 'Spirited Away', 'A young girl, Chihiro,
becomes trapped in a strange new world of spirits. When her parents undergo a mysterious transformation,
she must call upon the courage she never knew she had to free her family.', 'The tunnel led Chihiro to a mysterious town.
', '02:05:00', '2003-06-19', 274925095.00, 'https://www.themoviedb.org/t/p/w600_and_h900_bestv2/39wmItIWsg5sZMyRUHLkWBcuVCM.jpg');


INSERT INTO movie (id, title, overview, tagline, runtime, release_date, revenue, poster_path)
VALUES ('16e48282-21e3-4e6f-9bcc-a2f46f28b24d', 'Castle in the Sky', 'A young boy and a girl with a magic crystal must race against
pirates and foreign agents in a search for a legendary floating castle.', 'One day, a girl came down from the sky…',
        '02:05:00', '2006-06-09', 5228752.00, 'https://www.themoviedb.org/t/p/w600_and_h900_bestv2/npOnzAbLh6VOIu3naU5QaEcTepo.jpg');


INSERT INTO movie (id, title, overview, tagline, runtime, release_date, revenue, poster_path)
VALUES ('d40885c9-68a9-458d-bf66-f0b38ae2738b', 'Princess Mononoke', 'Ashitaka, a prince of the disappearing Emishi people, is cursed by a
demonized boar god and must journey to the west to find a cure. Along the way, he encounters San, a young human woman fighting to protect the forest,
and Lady Eboshi, who is trying to destroy it. Ashitaka must find a way to bring balance to this conflict.', 'The Fate Of The World Rests On The Courage Of One Warrior.',
        '02:14:00', '2000-07-25', 159414369.00, 'https://www.themoviedb.org/t/p/w600_and_h900_bestv2/cMYCDADoLKLbB83g4WnJegaZimC.jpg');


INSERT INTO movie (id, title, overview, tagline, runtime, release_date, revenue, poster_path)
VALUES ('bddce256-ed72-4ba4-9636-cea916eb53f0', 'My Neighbor Totoro', 'Two sisters move to the country with their father in order to be closer to their hospitalized mother,
and discover the surrounding trees are inhabited by Totoros, magical spirits of the forest.
When the youngest runs away from home, the older sister seeks help from the spirits to find her.', '',
        '02:05:00', '1988-04-16', 45000000.00, 'https://www.themoviedb.org/t/p/w600_and_h900_bestv2/rtGDOeG9LzoerkDGZF9dnVeLppL.jpg');


INSERT INTO movie (id, title, overview, tagline, runtime, release_date, revenue, poster_path)
VALUES ('85b0d4a1-acec-4a17-bfeb-6648ae0034f8', 'Howl''s Moving Castle', 'When Sophie, a shy young woman,
is cursed with an old body by a spiteful witch,
her only chance of breaking the spell lies with a self-indulgent yet insecure young wizard and his companions in his legged,
walking castle.', 'The two lived there.',
        '01:59:00', '2005-08-25', 236049757.00, 'https://www.themoviedb.org/t/p/w600_and_h900_bestv2/6pZgH10jhpToPcf0uvyTCPFhWpI.jpg');

INSERT INTO movie_review (id, author_name, content, rating, movie_id)
VALUES ('6619999c-b9f1-4227-971a-6de90123b2ed', 'Ze Blah', 'One of the great "masters" of the anime art.
Somehow, if I would personally associate "Akira" to "self-destruction",
then this anime would be the opposite :)',
        10, 'f74cf1ca-8c7b-435b-96c6-e4448a653596');

INSERT INTO movie_review (id, author_name, content, rating, movie_id)
VALUES ('993a44b5-da8b-4dad-9142-70ff0401d285', 'Andres Gomez', 'Really good movie staging in a
fictional center European country. The drawings are great and the story is as delicate as
any other from Miyazaki.',
        9, '85b0d4a1-acec-4a17-bfeb-6648ae0034f8');
