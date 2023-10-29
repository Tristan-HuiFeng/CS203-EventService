UPDATE event_seq
SET next_val = 101;

INSERT INTO EVENT (id, name, category, artist, description, banner_url, seat_map_url, location, is_featured, feature_sequence)
VALUES(0, "READY TO BE", "concert", "Twice", "Live Nation Singapore is thrilled to announce that South Korean girl group TWICE, comprising NAYEON, JEONGYEON, MOMO, SANA, JIHYO, MINA, DAHYUN, CHAEYOUNG and TZUYU, will return to Singapore after 4 years as part of their ongoing ‘READY TO BE’ world tour!",
		"https://cs203.s3.ap-southeast-1.amazonaws.com/event-image/TwiceConcertBanner.jpg", "https://cs203.s3.ap-southeast-1.amazonaws.com/seat-map/SeatMap.jpg",
		"Singapore Indoor Stadium", TRUE, 1);

INSERT INTO EVENT (id, name, category, artist, description, banner_url, seat_map_url, location, is_featured, feature_sequence)
VALUES(1, "Music Of The Spheres World Tour", "concert", "Coldplay", "Following the spectacular sellout success of Coldplay’s newly announced January and February 2024 dates in Asia, the band have today announced additional dates as part of their record-breaking Music Of The Spheres World Tour.

                         Already making history for announcing an unprecedented four-show run at Singapore’s largest venue, Coldplay will now be the first act ever to play five nights at Singapore’s National Stadium. The band also broke Singapore’s record for most tickets sold by an artist in a single day, surpassing 200,000.

                         Since the first Music Of The Spheres World Tour date in March 2022, the band have sold over 7 million tickets - the most for any tour over the last two years. The tour has also received rave reviews from fans and critics alike, picking up accolades including Favorite Touring Artist at the 2022 AMAs and Tour of The Year at the 2023 iHeartRadio Awards.

                         Earlier in June, the band issued an update on the tour’s sustainability initiatives, revealing that, on a show-by-show comparison, their current tour has so far produced 47% less CO2e emissions than their previous stadium tour in 2016/17, and that 5 million trees have already been planted around the world (one for each concert goer).

                         Coldplay have also confirmed they will make a limited number of Infinity Tickets available for the shows at a later date. Infinity Tickets are released for every Coldplay show to make the Music Of The Spheres World Tour accessible to fans for an affordable price. They will cost the equivalent of $20 USD per ticket. They are restricted to a maximum of two tickets per purchaser, and must be bought in pairs (which will be located next to each other).

                         DHL is the Official Logistics Partner of the Music Of The Spheres World Tour, supporting the band in their mission to cut the tour’s direct emissions by 50%.",

                 		"https://cs203.s3.ap-southeast-1.amazonaws.com/event-image/coldplay.png", "https://cs203.s3.ap-southeast-1.amazonaws.com/seat-map/SeatMap.jpg",
                 		"Singapore Indoor Stadium", TRUE, 2);

INSERT INTO EVENT (id, name, category, artist, description, banner_url, seat_map_url, location, is_featured, feature_sequence)
VALUES(2, "The “Charlie” Live Experience", "concert", "Charlie Puth", " Charlie Puth has proven to be one of the industry’s most consistent hitmakers and sought-after collaborators. Puth has amassed eight multi-platinum singles, four GRAMMY nominations, three Billboard Music Awards, a Critic’s Choice Award, and a Golden Globe nomination. His 2018 GRAMMY-nominated LP, “Voicenotes”, was RIAA Certified Gold only four days after its release and has logged over 5.6 billion streams worldwide.

                                               Recently, Puth released his highly anticipated third studio album, “CHARLIE” via Atlantic Records. Featuring hit singles “Left and Right [feat. Jung Kook of BTS], “That’s Hilarious” and “Light Switch,” the “expertly crafted collection” (ROLLING STONE) has surpassed 1 billion global streams and received critical praise around the world. Following the release of his “CHARLIE”, Puth set out for his ‘One Night Only’ tour, welcoming fans around the world up close and personal as he shares his latest album and his greatest hits.

                                               In 2020, Puth’s collaboration with Gabby Barrett on their “I Hope” remix earned him his fourth top 10 track on the Billboard Hot 100, hit number one on the Billboard “Adult Pop Songs” chart, and won a 2021 Billboard Music Award for “Top Collaboration”. Puth also co-wrote and produced The Kid Laroi and Justin Bieber’s record-breaking single, “Stay”, which quickly become one of the biggest songs of 2021 and holds the title for the longest-reigning No. 1 on the Billboard Global 200 chart and the first to lead it for double-digits - spending a total of eleven weeks at the top of the chart.",
		"https://cs203.s3.ap-southeast-1.amazonaws.com/event-image/charlieputh.jpeg", "https://cs203.s3.ap-southeast-1.amazonaws.com/seat-map/SeatMap.jpg",
		"Singapore Indoor Stadium", TRUE, 3);


INSERT INTO EVENT (id, name, category, artist, description, banner_url, seat_map_url, location, is_featured, feature_sequence)
VALUES(3, "HallyuPopFest Singapore 2023", "concert", "HallyuPopFest", " HallyuPopFest Singapore 2023, the highly anticipated K-Pop extravaganza, is set to make its grand return to Singapore this November! Fans will once again immerse themselves in the best of Korean music for one night only!

                                                                        K-Pop legend Taeyang who made his debut in 2006 as a member of the South Korean boyband BIGBANG, is set to deliver an electrifying performance that you won't want to miss. It’s been over 5 years since Taeyang’s last performance in Singapore, you can look forward to the live band performance of his latest songs like VIBE and Shoong! along with other popular hits.

                                                                        Other fan favorites on the line-up include SF9, Kwon Eunbi, Kep1er and DKZ. Don't miss this incredible opportunity to experience the sensational SF9, witness up-and-coming star Kwon Eunbi - a former member of IZ*ONE, Kep1er’s Singapore debut and support the promising journey of DKZ!",
		"https://cs203.s3.ap-southeast-1.amazonaws.com/event-image/Hallyupopfest.png", "https://cs203.s3.ap-southeast-1.amazonaws.com/seat-map/SeatMap.jpg",
		"Singapore Indoor Stadium", TRUE, 4);

INSERT INTO EVENT (id, name, category, artist, description, banner_url, seat_map_url, location, is_featured)
VALUES(4, "The Eras Tour", "concert", "Taylor Swift", "Taylor Swift announced additional dates to Taylor Swift | The Eras Tour today. Singapore will be the only stop in Southeast Asia. Taylor Swift | The Eras Tour in Singapore is presented by Marina Bay Sands and supported by the Singapore Tourism Board, official bank and pre-sale partner UOB, and official experience partner Klook, promoted by AEG Presents Asia, and produced by Taylor Swift Touring.",
		"https://cs203.s3.ap-southeast-1.amazonaws.com/event-image/taytay.jpeg", "https://cs203.s3.ap-southeast-1.amazonaws.com/seat-map/SeatMap.jpg",
		"Singapore Indoor Stadium", False);

INSERT INTO EVENT (id, name, category, artist, description, banner_url, seat_map_url, location, is_featured)
VALUES(5, "2023 (G)I-DLE WORLD TOUR [I am FREE-TY]", "concert", "(G)I-DLE", "iMe Singapore is thrilled to announce that Korean girl group (G)I-DLE will be bringing their world tour [I am FREE-TY] to Singapore!

                                                                             (G)I-DLE from CUBE Entertainment, made their debut successfully in 2018 with their title song “LATATA” of their first mini-album I am. Known as the \"Queen of Concepts\", the group released their first full album I NEVER DIE and 5th mini-album I love last year. The title songs \"TOMBOY\" and
                                                                             \"Nxde\" swept the world's major music charts and the Music videos of those 2 songs earned more than 200 million views on YouTube, which strongly proves their high popularity.

                                                                             (G)I-DLE announced their comeback with the release of their 6th mini-album I feel on 15th May. At the same time, they also announced their new world tour concert [I am FREE-TY], which kicked off in June starting from Seoul then followed by Asia, United States, and Europe. They will mark the end of their [I am FREE-TY] world tour here in Singapore, so be sure to look forward to a night of astonishing performances.",
		"https://cs203.s3.ap-southeast-1.amazonaws.com/event-image/gidle.jpg", "https://cs203.s3.ap-southeast-1.amazonaws.com/seat-map/SeatMap.jpg",
		"Singapore Indoor Stadium", False);

INSERT INTO EVENT (id, name, category, artist, description, banner_url, seat_map_url, location, is_featured)
VALUES(6, "ONE OK ROCK Luxury Disease Asia Tour 2023", "concert", "ONE OK ROCK", "ONE OK ROCK is a Japanese rock band formed in 2005 and debuted in 2007. Since then, the band has grown at a fast pace with immense support by the younger generation for its emo and rock-based sounds along with aggressive live performances.

                                                                                  They have rapidly become one of the most popular Japanese rock bands due to hugely successful concerts such as Nippon Budokan Live in 2010, Yokohama Stadium Live in 2014, and arena tours in 2013 & 2015. In 2016, they held an outdoor live concert that drew an astounding 110,000 people and in 2018, they held a nationwide dome tour including two days at Tokyo Dome.

                                                                                  In April 2015, the band signed a worldwide distribution deal with Warner Bros and have since held tours in the US, Europe, South America and Australia.

                                                                                  The band released the album “Eye of the Storm” in 2019 and were chosen as the opening act for Ed Sheeran’s Divide Asia Tour. In October 2020, the band successfully conducted their first attempt at an online live concert that was broadcast live simultaneously around the world without an audience. In July 2021, they completed their first acoustic live performance, sharing a more intimate side of their music. ONE OK ROCK will continue towards their goal to be Japan’s first globally recognized band and in turn have a strong impact on today’s younger generation and the Alternative music scene as a whole.",
		"https://cs203.s3.ap-southeast-1.amazonaws.com/event-image/oneokrock.jpg", "https://cs203.s3.ap-southeast-1.amazonaws.com/seat-map/SeatMap.jpg",
		"Singapore Indoor Stadium", False);

UPDATE admission_policy_seq
SET next_val = 101;

INSERT INTO ADMISSION_POLICY (id, event_id, policy_order, description)
VALUES (0, 1, 1, "Printed electronic tickets must be produced for admission.");

INSERT INTO ADMISSION_POLICY (id, event_id, policy_order, description)
VALUES (1, 1, 2, "There will be no admission for infants in arms and children below 3 years old.");

INSERT INTO ADMISSION_POLICY (id, event_id, policy_order, description)
VALUES (2, 1, 3, "Individuals aged 3 years old and above will be required to purchase a ticket for admission.");

INSERT INTO ADMISSION_POLICY (id, event_id, policy_order, description)
VALUES (3, 1, 4, "Please note that if you are children under 12 years old by date of birth, you are not allowed into the standing pen areas due to safety reasons.");

INSERT INTO ADMISSION_POLICY (id, event_id, policy_order, description)
VALUES (4, 1, 5, "Please note that if you are individual with height below 1.2 meters, you are not allowed into the standing pen areas due to safety reasons.");

INSERT INTO ADMISSION_POLICY (id, event_id, policy_order, description)
VALUES (5, 1, 6, "No photography, videography and social media live streaming allowed.");

INSERT INTO ADMISSION_POLICY (id, event_id, policy_order, description)
VALUES (6, 1, 7, "No outside food and beverage are allowed into the venue.");


UPDATE activity_seq
SET next_val = 101;

INSERT INTO ACTIVITY (id, start_datetime, end_datetime, event_id)
VALUES (0, "2023-12-17T12:00:00+08:00", "2023-12-17T16:00:00+08:00", 1);

INSERT INTO ACTIVITY (id, start_datetime, end_datetime, event_id)
VALUES (1, "2023-12-18T12:00:00+08:00", "2023-12-18T16:00:00+08:00", 1);

INSERT INTO ACTIVITY (id, start_datetime, end_datetime, event_id)
VALUES (2, "2023-12-19T12:00:00+08:00", "2023-12-19T16:00:00+08:00", 1);

INSERT INTO ACTIVITY (id, start_datetime, end_datetime, event_id)
VALUES (3, "2023-12-20T12:00:00+08:00", "2023-12-23T16:00:00+08:00", 0);

INSERT INTO ACTIVITY (id, start_datetime, end_datetime, event_id)
VALUES (4, "2024-01-15T12:00:00+08:00", "2023-01-17T16:00:00+08:00", 2);

INSERT INTO ACTIVITY (id, start_datetime, end_datetime, event_id)
VALUES (5, "2023-01-16T12:00:00+08:00", "2023-01-18T16:00:00+08:00", 3);

INSERT INTO ACTIVITY (id, start_datetime, end_datetime, event_id)
VALUES (6, "2023-01-19T12:00:00+08:00", "2023-01-21T16:00:00+08:00", 4);

INSERT INTO ACTIVITY (id, start_datetime, end_datetime, event_id)
VALUES (7, "2023-02-13T12:00:00+08:00", "2023-02-17T16:00:00+08:00", 5);

INSERT INTO ACTIVITY (id, start_datetime, end_datetime, event_id)
VALUES (8, "2023-02-20T12:00:00+08:00", "2023-02-25T16:00:00+08:00", 6);

UPDATE ticket_type_seq
SET next_val = 101;

INSERT INTO TICKET_TYPE (id, type, price, total_vacancy, reserved_count, occupied_count, activity_id)
VALUES (0, "CAT A", 300, 200, 0, 0, 0);

INSERT INTO TICKET_TYPE (id, type, price, total_vacancy, reserved_count, occupied_count, activity_id)
VALUES (1, "CAT B", 240, 350, 0, 0, 0);

INSERT INTO TICKET_TYPE (id, type, price, total_vacancy, reserved_count, occupied_count, activity_id)
VALUES (2, "CAT C", 200, 500, 0, 0, 0);


UPDATE sales_round_seq
SET next_val = 101;

INSERT INTO SALES_ROUND (id, sales_type, round_start, round_end, purchase_start, purchase_end, event_id)
VALUES (0, "Round 1 General", "2023-09-29T08:00:00+08:00", "2023-10-04T22:00:00+08:00", "2023-10-05T08:00:00+08:00", "2023-10-05T22:00:00+08:00", 1);

INSERT INTO SALES_ROUND (id, sales_type, round_start, round_end, purchase_start, purchase_end, event_id)
VALUES (1, "Round 2 General", "2023-10-06T08:00:00+08:00", "2023-10-11T22:00:00+08:00", "2023-10-12T08:00:00+08:00", "2023-10-12T22:00:00+08:00", 1);

INSERT INTO SALES_ROUND (id, sales_type, round_start, round_end, purchase_start, purchase_end, event_id)
VALUES (2, "Round 3 General", "2023-10-29T08:00:00+08:00", "2023-12-18T22:00:00+08:00", "2023-12-19T08:00:00+08:00", "2023-12-19T22:00:00+08:00", 1);


UPDATE ticket_sales_limit_seq
SET next_val = 101;

INSERT INTO TICKET_SALES_LIMIT (id, limit_vacancy, occupied_vacancy, sales_round_id, ticket_type_id)
VALUES (0,  100, 0, 0, 0);

INSERT INTO TICKET_SALES_LIMIT (id, limit_vacancy, occupied_vacancy, sales_round_id, ticket_type_id)
VALUES (1,  150, 0, 0, 1);

INSERT INTO TICKET_SALES_LIMIT (id, limit_vacancy, occupied_vacancy, sales_round_id, ticket_type_id)
VALUES (2,  200, 0, 0, 2);