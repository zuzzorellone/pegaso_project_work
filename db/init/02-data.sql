--
-- PostgreSQL database dump
--

\restrict yOzgYU3wDsr8bFavNftWnFs6graspOBh5mbLqcLyw2ISWucAi49hh0miGhuwCM2

-- Dumped from database version 16.13
-- Dumped by pg_dump version 16.13

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Data for Name: medical_examination_types; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.medical_examination_types VALUES (10, true, '17:00:00', 'Cardiologia', 30, 'CARDIOLOGY', '08:30:00');
INSERT INTO public.medical_examination_types VALUES (11, true, '13:00:00', 'Dermatologia', 20, 'DERMATOLOGY', '09:00:00');
INSERT INTO public.medical_examination_types VALUES (12, true, '10:30:00', 'Analisi del sangue', 15, 'BLOOD_TEST', '07:30:00');
INSERT INTO public.medical_examination_types VALUES (13, true, '18:00:00', 'Radiologia', 30, 'RADIOLOGY', '08:00:00');
INSERT INTO public.medical_examination_types VALUES (14, true, '19:00:00', 'Oculistica', 30, 'OPHTHALMOLOGY', '14:00:00');
INSERT INTO public.medical_examination_types VALUES (15, true, '16:00:00', 'Ortopedia', 30, 'ORTHOPEDICS', '09:00:00');


--
-- Data for Name: appointments; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.appointments VALUES (191, '2026-10-21 09:20:00', 'WAITING_FOR_CONFIRMATION', 45, 11, NULL);
INSERT INTO public.appointments VALUES (195, '2026-10-13 16:00:00', 'WAITING_FOR_REPORT', 53, 10, NULL);
INSERT INTO public.appointments VALUES (98, '2025-11-11 16:30:00', 'COMPLETED', 41, 10, NULL);
INSERT INTO public.appointments VALUES (99, '2025-11-20 13:30:00', 'COMPLETED', 41, 15, NULL);
INSERT INTO public.appointments VALUES (100, '2025-11-24 15:00:00', 'COMPLETED', 40, 15, NULL);
INSERT INTO public.appointments VALUES (101, '2025-11-26 09:45:00', 'COMPLETED', 41, 12, NULL);
INSERT INTO public.appointments VALUES (102, '2025-11-28 09:20:00', 'COMPLETED', 36, 11, NULL);
INSERT INTO public.appointments VALUES (103, '2025-12-01 09:45:00', 'COMPLETED', 34, 12, NULL);
INSERT INTO public.appointments VALUES (104, '2025-12-10 10:00:00', 'COMPLETED', 40, 10, NULL);
INSERT INTO public.appointments VALUES (105, '2025-12-25 18:30:00', 'COMPLETED', 40, 14, NULL);
INSERT INTO public.appointments VALUES (106, '2025-12-29 16:00:00', 'COMPLETED', 43, 10, NULL);
INSERT INTO public.appointments VALUES (107, '2026-01-03 09:30:00', 'COMPLETED', 41, 12, NULL);
INSERT INTO public.appointments VALUES (108, '2026-01-07 09:00:00', 'COMPLETED', 37, 12, NULL);
INSERT INTO public.appointments VALUES (109, '2026-01-10 09:15:00', 'COMPLETED', 35, 12, NULL);
INSERT INTO public.appointments VALUES (110, '2026-01-21 09:20:00', 'COMPLETED', 44, 11, NULL);
INSERT INTO public.appointments VALUES (111, '2026-01-21 14:30:00', 'COMPLETED', 44, 15, NULL);
INSERT INTO public.appointments VALUES (112, '2026-01-30 08:45:00', 'COMPLETED', 39, 12, NULL);
INSERT INTO public.appointments VALUES (113, '2026-02-10 08:00:00', 'COMPLETED', 44, 13, NULL);
INSERT INTO public.appointments VALUES (114, '2026-02-12 09:15:00', 'COMPLETED', 39, 12, NULL);
INSERT INTO public.appointments VALUES (115, '2026-02-13 16:00:00', 'COMPLETED', 34, 10, NULL);
INSERT INTO public.appointments VALUES (116, '2026-02-17 08:30:00', 'COMPLETED', 39, 12, NULL);
INSERT INTO public.appointments VALUES (117, '2026-02-17 09:45:00', 'COMPLETED', 40, 12, NULL);
INSERT INTO public.appointments VALUES (118, '2026-03-04 12:40:00', 'COMPLETED', 42, 11, NULL);
INSERT INTO public.appointments VALUES (119, '2026-03-04 14:00:00', 'COMPLETED', 44, 15, NULL);
INSERT INTO public.appointments VALUES (120, '2026-03-16 08:00:00', 'COMPLETED', 40, 12, NULL);
INSERT INTO public.appointments VALUES (121, '2026-03-16 16:30:00', 'COMPLETED', 37, 13, NULL);
INSERT INTO public.appointments VALUES (122, '2026-03-25 08:00:00', 'COMPLETED', 33, 12, NULL);
INSERT INTO public.appointments VALUES (123, '2026-03-26 08:00:00', 'COMPLETED', 42, 13, NULL);
INSERT INTO public.appointments VALUES (124, '2026-03-30 15:30:00', 'COMPLETED', 41, 13, NULL);
INSERT INTO public.appointments VALUES (125, '2026-04-10 08:30:00', 'COMPLETED', 33, 12, NULL);
INSERT INTO public.appointments VALUES (126, '2026-04-13 12:30:00', 'COMPLETED', 41, 13, NULL);
INSERT INTO public.appointments VALUES (127, '2026-04-16 11:30:00', 'COMPLETED', 36, 15, NULL);
INSERT INTO public.appointments VALUES (128, '2026-04-21 15:30:00', 'COMPLETED', 41, 10, NULL);
INSERT INTO public.appointments VALUES (129, '2026-04-21 18:00:00', 'COMPLETED', 44, 14, NULL);
INSERT INTO public.appointments VALUES (130, '2026-04-29 14:00:00', 'COMPLETED', 39, 13, NULL);
INSERT INTO public.appointments VALUES (131, '2026-05-04 08:15:00', 'COMPLETED', 44, 12, NULL);
INSERT INTO public.appointments VALUES (132, '2026-05-14 10:00:00', 'COMPLETED', 40, 15, NULL);
INSERT INTO public.appointments VALUES (133, '2026-05-15 07:45:00', 'COMPLETED', 40, 12, NULL);
INSERT INTO public.appointments VALUES (134, '2026-05-15 09:40:00', 'COMPLETED', 39, 11, NULL);
INSERT INTO public.appointments VALUES (135, '2026-05-21 11:00:00', 'COMPLETED', 40, 15, NULL);
INSERT INTO public.appointments VALUES (136, '2026-05-22 08:30:00', 'COMPLETED', 37, 13, NULL);
INSERT INTO public.appointments VALUES (137, '2026-05-23 09:15:00', 'COMPLETED', 40, 12, NULL);
INSERT INTO public.appointments VALUES (138, '2026-05-25 15:30:00', 'COMPLETED', 39, 13, NULL);
INSERT INTO public.appointments VALUES (139, '2026-06-01 09:30:00', 'COMPLETED', 33, 15, NULL);
INSERT INTO public.appointments VALUES (140, '2026-06-02 12:30:00', 'COMPLETED', 43, 10, NULL);
INSERT INTO public.appointments VALUES (141, '2026-06-05 11:40:00', 'COMPLETED', 36, 11, NULL);
INSERT INTO public.appointments VALUES (142, '2026-06-11 18:30:00', 'COMPLETED', 42, 14, NULL);
INSERT INTO public.appointments VALUES (143, '2026-06-19 16:30:00', 'COMPLETED', 34, 13, NULL);
INSERT INTO public.appointments VALUES (144, '2026-06-26 11:00:00', 'COMPLETED', 36, 13, NULL);
INSERT INTO public.appointments VALUES (145, '2026-06-30 17:30:00', 'COMPLETED', 44, 14, NULL);
INSERT INTO public.appointments VALUES (146, '2026-07-06 09:40:00', 'COMPLETED', 42, 11, NULL);
INSERT INTO public.appointments VALUES (147, '2026-07-07 17:00:00', 'COMPLETED', 33, 14, NULL);
INSERT INTO public.appointments VALUES (148, '2026-07-10 09:00:00', 'COMPLETED', 39, 13, NULL);
INSERT INTO public.appointments VALUES (149, '2026-07-13 09:30:00', 'COMPLETED', 33, 10, NULL);
INSERT INTO public.appointments VALUES (150, '2026-07-17 09:00:00', 'COMPLETED', 39, 12, NULL);
INSERT INTO public.appointments VALUES (151, '2026-07-31 17:30:00', 'COMPLETED', 44, 13, NULL);
INSERT INTO public.appointments VALUES (152, '2026-08-04 09:15:00', 'COMPLETED', 40, 12, NULL);
INSERT INTO public.appointments VALUES (153, '2026-08-11 12:30:00', 'COMPLETED', 40, 13, NULL);
INSERT INTO public.appointments VALUES (154, '2026-08-11 15:00:00', 'COMPLETED', 42, 13, NULL);
INSERT INTO public.appointments VALUES (155, '2026-08-12 14:00:00', 'COMPLETED', 38, 15, NULL);
INSERT INTO public.appointments VALUES (156, '2026-08-17 11:00:00', 'COMPLETED', 39, 13, NULL);
INSERT INTO public.appointments VALUES (157, '2026-08-20 10:30:00', 'COMPLETED', 35, 13, NULL);
INSERT INTO public.appointments VALUES (158, '2026-08-22 10:15:00', 'COMPLETED', 34, 12, NULL);
INSERT INTO public.appointments VALUES (159, '2026-08-25 09:00:00', 'COMPLETED', 33, 15, NULL);
INSERT INTO public.appointments VALUES (160, '2026-09-01 10:00:00', 'COMPLETED', 43, 15, NULL);
INSERT INTO public.appointments VALUES (161, '2026-09-02 11:00:00', 'COMPLETED', 43, 13, NULL);
INSERT INTO public.appointments VALUES (162, '2026-09-02 12:30:00', 'COMPLETED', 43, 15, NULL);
INSERT INTO public.appointments VALUES (163, '2026-09-02 14:00:00', 'COMPLETED', 41, 15, NULL);
INSERT INTO public.appointments VALUES (164, '2026-09-03 12:00:00', 'COMPLETED', 37, 15, NULL);
INSERT INTO public.appointments VALUES (165, '2026-09-07 16:00:00', 'COMPLETED', 35, 13, NULL);
INSERT INTO public.appointments VALUES (166, '2026-09-14 09:30:00', 'COMPLETED', 39, 12, NULL);
INSERT INTO public.appointments VALUES (167, '2026-09-16 10:15:00', 'COMPLETED', 43, 12, NULL);
INSERT INTO public.appointments VALUES (168, '2026-09-22 11:00:00', 'COMPLETED', 39, 10, NULL);
INSERT INTO public.appointments VALUES (169, '2026-10-01 09:30:00', 'WAITING_FOR_REPORT', 40, 15, NULL);
INSERT INTO public.appointments VALUES (170, '2026-10-06 11:00:00', 'COMPLETED', 37, 10, NULL);
INSERT INTO public.appointments VALUES (171, '2026-10-06 16:30:00', 'WAITING_FOR_REPORT', 40, 10, NULL);
INSERT INTO public.appointments VALUES (172, '2026-10-08 09:30:00', 'WAITING_FOR_REPORT', 41, 13, NULL);
INSERT INTO public.appointments VALUES (173, '2026-10-08 14:30:00', 'COMPLETED', 37, 13, NULL);
INSERT INTO public.appointments VALUES (174, '2026-10-09 08:30:00', 'WAITING_FOR_REPORT', 34, 12, NULL);
INSERT INTO public.appointments VALUES (175, '2026-10-12 10:00:00', 'COMPLETED', 35, 10, NULL);
INSERT INTO public.appointments VALUES (176, '2026-10-13 08:00:00', 'CONFIRMED', 35, 12, NULL);
INSERT INTO public.appointments VALUES (178, '2026-10-13 15:30:00', 'CONFIRMED', 33, 10, NULL);
INSERT INTO public.appointments VALUES (179, '2026-10-20 09:30:00', 'CONFIRMED', 42, 15, NULL);
INSERT INTO public.appointments VALUES (180, '2026-10-20 18:30:00', 'CONFIRMED', 37, 14, NULL);
INSERT INTO public.appointments VALUES (181, '2026-10-24 08:45:00', 'WAITING_FOR_CONFIRMATION', 44, 12, NULL);
INSERT INTO public.appointments VALUES (182, '2026-10-26 12:30:00', 'WAITING_FOR_CONFIRMATION', 34, 10, NULL);
INSERT INTO public.appointments VALUES (183, '2026-10-27 08:30:00', 'CANCELLED', 38, 12, NULL);
INSERT INTO public.appointments VALUES (184, '2026-10-28 09:20:00', 'WAITING_FOR_CONFIRMATION', 43, 11, NULL);
INSERT INTO public.appointments VALUES (185, '2026-11-04 11:00:00', 'CANCELLED', 42, 15, NULL);
INSERT INTO public.appointments VALUES (186, '2026-11-04 11:40:00', 'CONFIRMED', 35, 11, NULL);
INSERT INTO public.appointments VALUES (187, '2026-11-04 17:00:00', 'CONFIRMED', 41, 13, NULL);
INSERT INTO public.appointments VALUES (188, '2026-11-05 12:30:00', 'CONFIRMED', 41, 10, NULL);
INSERT INTO public.appointments VALUES (189, '2026-11-12 14:30:00', 'WAITING_FOR_CONFIRMATION', 41, 14, NULL);
INSERT INTO public.appointments VALUES (190, '2026-11-17 10:30:00', 'WAITING_FOR_CONFIRMATION', 43, 13, NULL);
INSERT INTO public.appointments VALUES (192, '2026-10-13 15:00:00', 'WAITING_FOR_CONFIRMATION', 47, 10, NULL);
INSERT INTO public.appointments VALUES (177, '2026-10-13 09:00:00', 'COMPLETED', 39, 10, NULL);


--
-- Data for Name: days_of_week; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.days_of_week VALUES (41, 'MONDAY', 10);
INSERT INTO public.days_of_week VALUES (42, 'TUESDAY', 10);
INSERT INTO public.days_of_week VALUES (43, 'WEDNESDAY', 10);
INSERT INTO public.days_of_week VALUES (44, 'THURSDAY', 10);
INSERT INTO public.days_of_week VALUES (45, 'FRIDAY', 10);
INSERT INTO public.days_of_week VALUES (46, 'MONDAY', 11);
INSERT INTO public.days_of_week VALUES (47, 'WEDNESDAY', 11);
INSERT INTO public.days_of_week VALUES (48, 'FRIDAY', 11);
INSERT INTO public.days_of_week VALUES (49, 'MONDAY', 12);
INSERT INTO public.days_of_week VALUES (50, 'TUESDAY', 12);
INSERT INTO public.days_of_week VALUES (51, 'WEDNESDAY', 12);
INSERT INTO public.days_of_week VALUES (52, 'THURSDAY', 12);
INSERT INTO public.days_of_week VALUES (53, 'FRIDAY', 12);
INSERT INTO public.days_of_week VALUES (54, 'SATURDAY', 12);
INSERT INTO public.days_of_week VALUES (55, 'MONDAY', 13);
INSERT INTO public.days_of_week VALUES (56, 'TUESDAY', 13);
INSERT INTO public.days_of_week VALUES (57, 'WEDNESDAY', 13);
INSERT INTO public.days_of_week VALUES (58, 'THURSDAY', 13);
INSERT INTO public.days_of_week VALUES (59, 'FRIDAY', 13);
INSERT INTO public.days_of_week VALUES (60, 'TUESDAY', 14);
INSERT INTO public.days_of_week VALUES (61, 'THURSDAY', 14);
INSERT INTO public.days_of_week VALUES (62, 'MONDAY', 15);
INSERT INTO public.days_of_week VALUES (63, 'TUESDAY', 15);
INSERT INTO public.days_of_week VALUES (64, 'WEDNESDAY', 15);
INSERT INTO public.days_of_week VALUES (65, 'THURSDAY', 15);


--
-- Data for Name: patients; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.patients VALUES (33, 'marco.rossi@example.com', 'Marco', 'Rossi', 'RSSMRC78C12H501K', '3403707689');
INSERT INTO public.patients VALUES (34, 'giulia.romano@example.com', 'Giulia', 'Romano', 'RMNGLI85D52F205T', '3415376476');
INSERT INTO public.patients VALUES (35, 'alessandro.esposito@example.com', 'Alessandro', 'Esposito', 'SPSLSN90E14F839W', '3425297049');
INSERT INTO public.patients VALUES (36, 'chiara.ricci@example.com', 'Chiara', 'Ricci', 'RCCCHR92M45H501B', '3432054935');
INSERT INTO public.patients VALUES (37, 'luca.moretti@example.com', 'Luca', 'Moretti', 'MRTLCU69A20L219P', '3441232708');
INSERT INTO public.patients VALUES (38, 'valentina.barbieri@example.com', 'Valentina', 'Barbieri', 'BRBVLN88T60H501N', '3457906212');
INSERT INTO public.patients VALUES (39, 'andrea.fontana@example.com', 'Andrea', 'Fontana', 'FNTNDR75P03A944H', '3465312449');
INSERT INTO public.patients VALUES (40, 'martina.santoro@example.com', 'Martina', 'Santoro', 'SNTMTN95R41H501C', '3477954630');
INSERT INTO public.patients VALUES (41, 'roberto.mariani@example.com', 'Roberto', 'Mariani', 'MRNRRT61S08H501F', '3481258872');
INSERT INTO public.patients VALUES (42, 'federica.rinaldi@example.com', 'Federica', 'Rinaldi', 'RNLFDR83H49G273Y', '3491961529');
INSERT INTO public.patients VALUES (43, 'simone.caruso@example.com', 'Simone', 'Caruso', 'CRSSMN97B11H501J', '3502618161');
INSERT INTO public.patients VALUES (44, 'anna.lombardi@example.com', 'Anna', 'Lombardi', 'LMBNNA56L67H501D', '3514946427');
INSERT INTO public.patients VALUES (45, 'marco.rossi@mail.it', 'Marco', 'Rossi', 'RSSMRC85C15F205K', '321987654');
INSERT INTO public.patients VALUES (47, 'giulia.bianchi@mail.it', 'Giulia', 'Bianchi', 'BNCGLI93L62H501Q', '321987654321');
INSERT INTO public.patients VALUES (53, 'alessandro.ferrari@mail.it', 'Alessandro', 'Ferrari', 'FRRLSN01S08L219Y', '321987654321');


--
-- Data for Name: reports; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.reports VALUES (82, 111, 'referto_lombardi_20260121.pdf', '/home/boarbyte/medical_uploads/9b37aa37-8f22-4f34-aa6b-44a5dec6d9e5.pdf', 46);
INSERT INTO public.reports VALUES (69, 98, 'referto_mariani_20251111.pdf', '/home/boarbyte/medical_uploads/7db44f3c-a623-4187-846b-a387c1dfa12b.pdf', 2);
INSERT INTO public.reports VALUES (70, 99, 'referto_mariani_20251120.pdf', '/home/boarbyte/medical_uploads/3bef15ca-3cfb-4814-8228-320b4f99b8c8.pdf', 46);
INSERT INTO public.reports VALUES (71, 100, 'referto_santoro_20251124.pdf', '/home/boarbyte/medical_uploads/f6bd2381-278d-4f6c-9061-6f2de05f64f9.pdf', 46);
INSERT INTO public.reports VALUES (72, 101, 'referto_mariani_20251126.pdf', '/home/boarbyte/medical_uploads/c21e51a2-6727-4a52-87c6-7660b598ca7f.pdf', 42);
INSERT INTO public.reports VALUES (73, 102, 'referto_ricci_20251128.pdf', '/home/boarbyte/medical_uploads/11bba23d-a3e1-44f3-8b89-08cadd11d05f.pdf', 41);
INSERT INTO public.reports VALUES (74, 103, 'referto_romano_20251201.pdf', '/home/boarbyte/medical_uploads/5ece6082-d291-4cb3-8f47-30ad48b99e2e.pdf', 42);
INSERT INTO public.reports VALUES (75, 104, 'referto_santoro_20251210.pdf', '/home/boarbyte/medical_uploads/0933beba-ec95-4cc3-8897-f311496fbd9e.pdf', 2);
INSERT INTO public.reports VALUES (76, 105, 'referto_santoro_20251225.pdf', '/home/boarbyte/medical_uploads/d50ea53f-ab59-4150-b276-81d7c8ccb49a.pdf', 45);
INSERT INTO public.reports VALUES (77, 106, 'referto_caruso_20251229.pdf', '/home/boarbyte/medical_uploads/7419bbb1-ff6f-4cbb-b277-f75ac02800fc.pdf', 2);
INSERT INTO public.reports VALUES (78, 107, 'referto_mariani_20260103.pdf', '/home/boarbyte/medical_uploads/d3da249d-653c-4301-ae2e-9ac7e9cab90b.pdf', 42);
INSERT INTO public.reports VALUES (79, 108, 'referto_moretti_20260107.pdf', '/home/boarbyte/medical_uploads/fbf15768-f1aa-4e94-a805-89f04a7951c0.pdf', 42);
INSERT INTO public.reports VALUES (80, 109, 'referto_esposito_20260110.pdf', '/home/boarbyte/medical_uploads/0b8618c9-dda3-4997-a7fd-dcb96ae8eb60.pdf', 42);
INSERT INTO public.reports VALUES (81, 110, 'referto_lombardi_20260121.pdf', '/home/boarbyte/medical_uploads/0f4d5b2f-c176-4929-a6f6-ec9a15e1b38b.pdf', 41);
INSERT INTO public.reports VALUES (83, 112, 'referto_fontana_20260130.pdf', '/home/boarbyte/medical_uploads/21d6d4d1-fd56-4d16-999c-2c77b74d1436.pdf', 42);
INSERT INTO public.reports VALUES (84, 113, 'referto_lombardi_20260210.pdf', '/home/boarbyte/medical_uploads/78af371a-52f9-4d53-8773-2565d0ff432f.pdf', 43);
INSERT INTO public.reports VALUES (85, 114, 'referto_fontana_20260212.pdf', '/home/boarbyte/medical_uploads/9a38dd20-1d2e-4aeb-a6cf-12c5bf5cb3a2.pdf', 42);
INSERT INTO public.reports VALUES (86, 115, 'referto_romano_20260213.pdf', '/home/boarbyte/medical_uploads/8f21fce2-23d7-4fdc-87cb-a5edf06dce63.pdf', 39);
INSERT INTO public.reports VALUES (87, 116, 'referto_fontana_20260217.pdf', '/home/boarbyte/medical_uploads/e8a0be2c-7cd4-4778-9f39-434732892118.pdf', 42);
INSERT INTO public.reports VALUES (88, 117, 'referto_santoro_20260217.pdf', '/home/boarbyte/medical_uploads/c07c0fa6-5986-4b8b-84f2-2508089f1912.pdf', 42);
INSERT INTO public.reports VALUES (89, 118, 'referto_rinaldi_20260304.pdf', '/home/boarbyte/medical_uploads/e2816889-26ce-4cf1-9111-e5f90bf9bd31.pdf', 41);
INSERT INTO public.reports VALUES (90, 119, 'referto_lombardi_20260304.pdf', '/home/boarbyte/medical_uploads/267f6559-ff7c-4a31-96fe-a9fb184c49d6.pdf', 46);
INSERT INTO public.reports VALUES (91, 120, 'referto_santoro_20260316.pdf', '/home/boarbyte/medical_uploads/07c6e327-3c76-4835-b9bb-1727f77a11a6.pdf', 42);
INSERT INTO public.reports VALUES (92, 121, 'referto_moretti_20260316.pdf', '/home/boarbyte/medical_uploads/b2faa3aa-c560-43dd-9d3a-6aa6a911ef37.pdf', 44);
INSERT INTO public.reports VALUES (93, 122, 'referto_rossi_20260325.pdf', '/home/boarbyte/medical_uploads/7d727f14-2956-4e2b-9f63-f74af0744b8e.pdf', 42);
INSERT INTO public.reports VALUES (94, 123, 'referto_rinaldi_20260326.pdf', '/home/boarbyte/medical_uploads/fb7ee1c5-18b0-4bdc-9391-927cb9d39d77.pdf', 43);
INSERT INTO public.reports VALUES (95, 124, 'referto_mariani_20260330.pdf', '/home/boarbyte/medical_uploads/09510c04-1e88-460a-be85-44f396430c03.pdf', 43);
INSERT INTO public.reports VALUES (96, 125, 'referto_rossi_20260410.pdf', '/home/boarbyte/medical_uploads/d8221917-8596-4b02-a33a-afd284379bfa.pdf', 42);
INSERT INTO public.reports VALUES (97, 126, 'referto_mariani_20260413.pdf', '/home/boarbyte/medical_uploads/737510a8-466b-48ba-a1e7-7b4a737f47b5.pdf', 43);
INSERT INTO public.reports VALUES (98, 127, 'referto_ricci_20260416.pdf', '/home/boarbyte/medical_uploads/75725870-1d92-4e7d-96e0-8ed426ecdcdf.pdf', 46);
INSERT INTO public.reports VALUES (99, 128, 'referto_mariani_20260421.pdf', '/home/boarbyte/medical_uploads/420591c8-1172-40ec-a123-34944d442197.pdf', 39);
INSERT INTO public.reports VALUES (100, 129, 'referto_lombardi_20260421.pdf', '/home/boarbyte/medical_uploads/9c70c579-dde0-4c82-b850-c77cb06c930f.pdf', 45);
INSERT INTO public.reports VALUES (101, 130, 'referto_fontana_20260429.pdf', '/home/boarbyte/medical_uploads/cc6048a4-6c32-42db-b8ec-ac214fe9afc4.pdf', 44);
INSERT INTO public.reports VALUES (102, 131, 'referto_lombardi_20260504.pdf', '/home/boarbyte/medical_uploads/9be325da-091a-493e-9c21-4e6e278513f4.pdf', 42);
INSERT INTO public.reports VALUES (103, 132, 'referto_santoro_20260514.pdf', '/home/boarbyte/medical_uploads/76c93d21-65e0-4266-a3f2-08be4cc47a26.pdf', 46);
INSERT INTO public.reports VALUES (104, 133, 'referto_santoro_20260515.pdf', '/home/boarbyte/medical_uploads/d067e514-9fc5-40c5-962f-82363be30a45.pdf', 42);
INSERT INTO public.reports VALUES (105, 134, 'referto_fontana_20260515.pdf', '/home/boarbyte/medical_uploads/15b9161b-de3f-4d6b-a39f-f86955dc39bb.pdf', 41);
INSERT INTO public.reports VALUES (106, 135, 'referto_santoro_20260521.pdf', '/home/boarbyte/medical_uploads/41a26461-b5d0-4123-af4c-ef26822be8da.pdf', 46);
INSERT INTO public.reports VALUES (107, 136, 'referto_moretti_20260522.pdf', '/home/boarbyte/medical_uploads/cc2706bb-a4c7-4057-a587-967a7c9c3e33.pdf', 43);
INSERT INTO public.reports VALUES (108, 137, 'referto_santoro_20260523.pdf', '/home/boarbyte/medical_uploads/685bb254-75e1-4bb8-94be-19c10379e0af.pdf', 42);
INSERT INTO public.reports VALUES (109, 138, 'referto_fontana_20260525.pdf', '/home/boarbyte/medical_uploads/ed1d335c-1072-4efa-af7d-5da26e9802c2.pdf', 44);
INSERT INTO public.reports VALUES (110, 139, 'referto_rossi_20260601.pdf', '/home/boarbyte/medical_uploads/1c882afd-dbfe-4db4-b424-6d0bae1170c7.pdf', 46);
INSERT INTO public.reports VALUES (111, 140, 'referto_caruso_20260602.pdf', '/home/boarbyte/medical_uploads/c74144ce-efae-4d01-8b53-79e813d283bc.pdf', 2);
INSERT INTO public.reports VALUES (112, 141, 'referto_ricci_20260605.pdf', '/home/boarbyte/medical_uploads/54acb549-0b83-4829-9a41-973c253cdbdb.pdf', 40);
INSERT INTO public.reports VALUES (113, 142, 'referto_rinaldi_20260611.pdf', '/home/boarbyte/medical_uploads/2ebbb209-3968-4f3c-a820-271d7edf17da.pdf', 45);
INSERT INTO public.reports VALUES (114, 143, 'referto_romano_20260619.pdf', '/home/boarbyte/medical_uploads/c713a4c0-799b-41d2-a4d8-68ceef71e521.pdf', 44);
INSERT INTO public.reports VALUES (115, 144, 'referto_ricci_20260626.pdf', '/home/boarbyte/medical_uploads/aac27044-8bc3-42b9-818a-7c5de45e1aa2.pdf', 44);
INSERT INTO public.reports VALUES (116, 145, 'referto_lombardi_20260630.pdf', '/home/boarbyte/medical_uploads/524317f8-adb1-4804-8035-e0271c843905.pdf', 45);
INSERT INTO public.reports VALUES (117, 146, 'referto_rinaldi_20260706.pdf', '/home/boarbyte/medical_uploads/46720d61-a95f-47f9-8154-efca812226a1.pdf', 41);
INSERT INTO public.reports VALUES (118, 147, 'referto_rossi_20260707.pdf', '/home/boarbyte/medical_uploads/31d7bfda-2f7d-4c41-b9b5-133233491e7d.pdf', 45);
INSERT INTO public.reports VALUES (119, 148, 'referto_fontana_20260710.pdf', '/home/boarbyte/medical_uploads/0ee0d4c9-e1a4-4830-92bb-b84ca8b67a5a.pdf', 43);
INSERT INTO public.reports VALUES (120, 149, 'referto_rossi_20260713.pdf', '/home/boarbyte/medical_uploads/d5cfcff1-6e3c-4b70-a0c1-a73531a1db15.pdf', 39);
INSERT INTO public.reports VALUES (121, 150, 'referto_fontana_20260717.pdf', '/home/boarbyte/medical_uploads/a7853118-b287-4ff5-a080-b703085f4267.pdf', 42);
INSERT INTO public.reports VALUES (122, 151, 'referto_lombardi_20260731.pdf', '/home/boarbyte/medical_uploads/57453ec3-d1ea-49f8-80be-1217ca125460.pdf', 43);
INSERT INTO public.reports VALUES (123, 152, 'referto_santoro_20260804.pdf', '/home/boarbyte/medical_uploads/a101974a-0f5d-4bd8-941f-961c7b8ab1fd.pdf', 42);
INSERT INTO public.reports VALUES (124, 153, 'referto_santoro_20260811.pdf', '/home/boarbyte/medical_uploads/938a81f7-b82f-4358-ae21-d5060302d236.pdf', 43);
INSERT INTO public.reports VALUES (125, 154, 'referto_rinaldi_20260811.pdf', '/home/boarbyte/medical_uploads/dcf3cc4f-44fd-4161-9041-564b67f1f353.pdf', 43);
INSERT INTO public.reports VALUES (126, 155, 'referto_barbieri_20260812.pdf', '/home/boarbyte/medical_uploads/db55e927-221b-4974-a1f2-4feea0454487.pdf', 46);
INSERT INTO public.reports VALUES (127, 156, 'referto_fontana_20260817.pdf', '/home/boarbyte/medical_uploads/1c486088-4f40-41db-b0ad-a9f3c68996ce.pdf', 43);
INSERT INTO public.reports VALUES (128, 157, 'referto_esposito_20260820.pdf', '/home/boarbyte/medical_uploads/028a4f6f-9e8b-4c21-b683-0e1e31b9fcd6.pdf', 43);
INSERT INTO public.reports VALUES (129, 158, 'referto_romano_20260822.pdf', '/home/boarbyte/medical_uploads/1e84678d-0109-4878-94d8-2da53fde4a47.pdf', 42);
INSERT INTO public.reports VALUES (130, 159, 'referto_rossi_20260825.pdf', '/home/boarbyte/medical_uploads/976e0742-2f76-4135-8f16-7b17c72643a2.pdf', 46);
INSERT INTO public.reports VALUES (131, 160, 'referto_caruso_20260901.pdf', '/home/boarbyte/medical_uploads/0be902cf-2c6f-4379-8335-d5f5820fc4d1.pdf', 46);
INSERT INTO public.reports VALUES (132, 161, 'referto_caruso_20260902.pdf', '/home/boarbyte/medical_uploads/3e02050c-aa66-4207-a7aa-64b41a617ecc.pdf', 43);
INSERT INTO public.reports VALUES (133, 162, 'referto_caruso_20260902.pdf', '/home/boarbyte/medical_uploads/026db653-0d65-49fd-ae86-e3af134b9678.pdf', 46);
INSERT INTO public.reports VALUES (134, 163, 'referto_mariani_20260902.pdf', '/home/boarbyte/medical_uploads/7dbb090f-f789-4d45-a715-f8c04655695b.pdf', 46);
INSERT INTO public.reports VALUES (135, 164, 'referto_moretti_20260903.pdf', '/home/boarbyte/medical_uploads/ec812951-ebdd-4162-888b-5aed434e08a0.pdf', 46);
INSERT INTO public.reports VALUES (136, 165, 'referto_esposito_20260907.pdf', '/home/boarbyte/medical_uploads/46ecd666-4a36-431b-b80c-9a228b84d7b9.pdf', 43);
INSERT INTO public.reports VALUES (137, 166, 'referto_fontana_20260914.pdf', '/home/boarbyte/medical_uploads/24b6693b-7116-4410-8430-5ee6f2309db6.pdf', 42);
INSERT INTO public.reports VALUES (138, 167, 'referto_caruso_20260916.pdf', '/home/boarbyte/medical_uploads/564a3d2a-4f40-4634-8b57-d1d1cac70e2c.pdf', 42);
INSERT INTO public.reports VALUES (139, 168, 'referto_fontana_20260922.pdf', '/home/boarbyte/medical_uploads/b71b2cb4-e44c-4fb2-b076-171bb59a4342.pdf', 2);
INSERT INTO public.reports VALUES (140, 170, 'referto_moretti_20261006.pdf', '/home/boarbyte/medical_uploads/fa13720c-d93c-4d46-b618-f502faefc4fd.pdf', 2);
INSERT INTO public.reports VALUES (141, 173, 'referto_moretti_20261008.pdf', '/home/boarbyte/medical_uploads/b7abef73-5487-4439-bd85-5540499e01fd.pdf', 44);
INSERT INTO public.reports VALUES (142, 175, 'referto_esposito_20261012.pdf', '/home/boarbyte/medical_uploads/c3f55b38-2471-497e-91b7-1ff89c98508c.pdf', 39);
INSERT INTO public.reports VALUES (143, 177, 'referto_Fontana_20261013.pdf', '/home/boarbyte/medical_uploads/1409ff1b-05a7-41e6-a10f-080e00d4be20.pdf', 39);


--
-- Data for Name: reports_tokens; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.reports_tokens VALUES (200, '2026-10-15 12:57:54.623188', 0, '1c4c1bce-a697-4dee-8fe5-134ab8576692', 143);
INSERT INTO public.reports_tokens VALUES (125, '2025-11-14 18:30:00', 0, '34431305-3dba-4ebe-b6b1-e43835ad3d6f', 69);
INSERT INTO public.reports_tokens VALUES (126, '2025-11-22 15:30:00', 0, '2282f148-0252-42c4-b95f-5fca88922777', 70);
INSERT INTO public.reports_tokens VALUES (127, '2025-11-27 17:00:00', 0, '87f50564-2f73-4b12-9b72-f2bad2b35159', 71);
INSERT INTO public.reports_tokens VALUES (128, '2025-11-30 11:45:00', 0, '6d0722e7-3a37-4e4b-b62d-55bb5957f1fd', 72);
INSERT INTO public.reports_tokens VALUES (129, '2025-12-01 11:20:00', 0, 'cb66ed88-3fe3-4018-b09b-85df421363f6', 73);
INSERT INTO public.reports_tokens VALUES (130, '2025-12-05 11:45:00', 0, '34288a44-a425-4774-a09f-ab33fc520d26', 74);
INSERT INTO public.reports_tokens VALUES (131, '2025-12-14 12:00:00', 0, '9e534f65-fc8e-45f1-bd62-e0fcd8cf4a12', 75);
INSERT INTO public.reports_tokens VALUES (132, '2025-12-28 20:30:00', 0, 'f0f9bcad-c7a7-4747-bbc1-54047475fcef', 76);
INSERT INTO public.reports_tokens VALUES (133, '2026-01-02 18:00:00', 0, '2be9d0ec-4f64-406e-b5fc-6739009b9aed', 77);
INSERT INTO public.reports_tokens VALUES (134, '2026-01-07 11:30:00', 0, 'b4df3292-0869-4104-a925-b0339c871776', 78);
INSERT INTO public.reports_tokens VALUES (135, '2026-01-10 11:00:00', 0, 'c365ac6e-f3fd-42ea-bd78-b0ef561eaadb', 79);
INSERT INTO public.reports_tokens VALUES (136, '2026-01-13 11:15:00', 0, 'c1970ce7-7057-40d8-a8df-3c223c35a7ff', 80);
INSERT INTO public.reports_tokens VALUES (137, '2026-01-25 11:20:00', 0, '1f809d6b-5ffa-4083-9be0-c6720e58492b', 81);
INSERT INTO public.reports_tokens VALUES (138, '2026-01-23 16:30:00', 0, '4fdb8f9a-d055-4f1d-b380-9fadf33e4508', 82);
INSERT INTO public.reports_tokens VALUES (139, '2026-02-03 10:45:00', 0, '596e9d97-bcda-4c94-a10c-a7e5f15cb364', 83);
INSERT INTO public.reports_tokens VALUES (140, '2026-02-13 10:00:00', 0, 'c726d800-a678-4897-9818-03d39ccacd73', 84);
INSERT INTO public.reports_tokens VALUES (141, '2026-02-15 11:15:00', 0, '22d74628-d04d-40b7-9902-1e6e74ba054e', 85);
INSERT INTO public.reports_tokens VALUES (142, '2026-02-16 18:00:00', 0, 'b9b139b7-9832-45cf-b02b-83a7710e034a', 86);
INSERT INTO public.reports_tokens VALUES (143, '2026-02-21 10:30:00', 0, '02df90c1-d1f3-4834-950d-f6de618a8f95', 87);
INSERT INTO public.reports_tokens VALUES (144, '2026-02-19 11:45:00', 0, '06f799c4-829d-4822-928b-bee2a3dba948', 88);
INSERT INTO public.reports_tokens VALUES (145, '2026-03-08 14:40:00', 0, '9ab97759-8131-4c15-bed4-1fbc1f1075a5', 89);
INSERT INTO public.reports_tokens VALUES (146, '2026-03-06 16:00:00', 0, 'e5acc5c2-84bd-461c-bd54-20ad31615c1d', 90);
INSERT INTO public.reports_tokens VALUES (147, '2026-03-18 10:00:00', 0, '29d21fac-8eed-4bc4-8f5d-3cf8c246f6ab', 91);
INSERT INTO public.reports_tokens VALUES (148, '2026-03-18 18:30:00', 0, '5e6a1e85-345e-4972-92cc-aa9bbfa79968', 92);
INSERT INTO public.reports_tokens VALUES (149, '2026-03-29 10:00:00', 0, '734e3cff-28c6-45a9-8ddd-956d3c19dfff', 93);
INSERT INTO public.reports_tokens VALUES (150, '2026-03-28 10:00:00', 0, '26c39266-6732-4b69-92e9-b28e3f075201', 94);
INSERT INTO public.reports_tokens VALUES (151, '2026-04-03 17:30:00', 0, '7378be37-97a1-48c8-a298-1e1ff4963988', 95);
INSERT INTO public.reports_tokens VALUES (152, '2026-04-13 10:30:00', 0, 'd32cb047-c1a1-4e5a-a258-aa73de82c0f6', 96);
INSERT INTO public.reports_tokens VALUES (153, '2026-04-16 14:30:00', 0, 'b4155c66-d188-4ca5-889c-46496ea78f64', 97);
INSERT INTO public.reports_tokens VALUES (154, '2026-04-18 13:30:00', 0, 'c77b47b7-04e3-4417-ae7a-5e10791850ec', 98);
INSERT INTO public.reports_tokens VALUES (155, '2026-04-25 17:30:00', 0, '24982632-14d4-417d-8ce7-3900e7698e48', 99);
INSERT INTO public.reports_tokens VALUES (156, '2026-04-23 20:00:00', 0, 'bb7e9a65-a2b3-4c8a-8b7a-1167d7d9b986', 100);
INSERT INTO public.reports_tokens VALUES (157, '2026-05-03 16:00:00', 0, '230e497c-1eb7-4122-940a-57fff498c954', 101);
INSERT INTO public.reports_tokens VALUES (158, '2026-05-08 10:15:00', 0, '4f0d7413-fd72-410a-be37-eeb89a04ce5d', 102);
INSERT INTO public.reports_tokens VALUES (159, '2026-05-17 12:00:00', 0, '9f0c84cd-097c-434e-9373-4edebd6ab186', 103);
INSERT INTO public.reports_tokens VALUES (160, '2026-05-17 09:45:00', 0, 'e17e7ffc-973c-4e6e-a5dd-e9c34438d1e4', 104);
INSERT INTO public.reports_tokens VALUES (161, '2026-05-17 11:40:00', 0, '57ec6eec-c9ff-4474-8a4d-e3a462366017', 105);
INSERT INTO public.reports_tokens VALUES (162, '2026-05-25 13:00:00', 0, 'd129a5aa-ac3d-40f7-9cfd-2dddb6448824', 106);
INSERT INTO public.reports_tokens VALUES (163, '2026-05-25 10:30:00', 0, 'fadfc63d-1cef-408e-97be-c7878814f1cd', 107);
INSERT INTO public.reports_tokens VALUES (164, '2026-05-26 11:15:00', 0, '5cf01b35-ad47-488e-bf69-e1fff52977da', 108);
INSERT INTO public.reports_tokens VALUES (165, '2026-05-29 17:30:00', 0, 'f5513bc0-3883-4fd0-93b0-29a827eb4878', 109);
INSERT INTO public.reports_tokens VALUES (166, '2026-06-03 11:30:00', 0, 'a39dc85a-9295-4f98-ab26-fa621721b7bc', 110);
INSERT INTO public.reports_tokens VALUES (167, '2026-06-06 14:30:00', 0, '7319660f-c8aa-4608-ab22-83648f7cf7c1', 111);
INSERT INTO public.reports_tokens VALUES (168, '2026-06-08 13:40:00', 0, 'd8b45a15-f4af-45a4-86ec-b6973da346a4', 112);
INSERT INTO public.reports_tokens VALUES (169, '2026-06-13 20:30:00', 0, 'cba3e247-8206-4cd5-b985-9bef799a30f4', 113);
INSERT INTO public.reports_tokens VALUES (170, '2026-06-23 18:30:00', 0, '5b058128-f237-47d7-b6f6-fd3d8e8cc31f', 114);
INSERT INTO public.reports_tokens VALUES (171, '2026-06-29 13:00:00', 0, '4f593ee7-532d-4560-93bb-11b7a7e2bfee', 115);
INSERT INTO public.reports_tokens VALUES (172, '2026-07-02 19:30:00', 0, '8d5a8b1b-b391-4acd-a191-9eae7391e2fe', 116);
INSERT INTO public.reports_tokens VALUES (173, '2026-07-10 11:40:00', 0, 'e6bd619e-4bff-465d-8c9b-b82ecca9f533', 117);
INSERT INTO public.reports_tokens VALUES (174, '2026-07-11 19:00:00', 0, 'ac81f25f-337d-4530-ba9d-cba211b64ded', 118);
INSERT INTO public.reports_tokens VALUES (175, '2026-07-12 11:00:00', 0, '2d89ebb4-b8db-4ffe-be1b-37fba8c3dbf4', 119);
INSERT INTO public.reports_tokens VALUES (176, '2026-07-16 11:30:00', 0, '4dc5ebfc-1338-4756-acd5-e820038bb7fb', 120);
INSERT INTO public.reports_tokens VALUES (177, '2026-07-20 11:00:00', 0, 'f49f8d24-7b0c-4b97-b3a7-7a1200106b77', 121);
INSERT INTO public.reports_tokens VALUES (178, '2026-08-03 19:30:00', 0, 'b09efb4d-04de-436a-a573-8f2be3512dd2', 122);
INSERT INTO public.reports_tokens VALUES (179, '2026-08-08 11:15:00', 0, 'ca4eccb7-80ee-41cc-8f2e-26e09bc13551', 123);
INSERT INTO public.reports_tokens VALUES (180, '2026-08-15 14:30:00', 0, '94e6d024-c177-4f64-9e9c-6c12855a53d6', 124);
INSERT INTO public.reports_tokens VALUES (181, '2026-08-13 17:00:00', 0, 'bd0ccbf5-acc6-4d67-bb9b-c001702f1d18', 125);
INSERT INTO public.reports_tokens VALUES (182, '2026-08-16 16:00:00', 0, '9b6e418a-bff7-4f87-9020-6efb1b63d1d8', 126);
INSERT INTO public.reports_tokens VALUES (183, '2026-08-19 13:00:00', 0, 'd9c43886-827b-42ea-87d1-6649f24f07b7', 127);
INSERT INTO public.reports_tokens VALUES (184, '2026-08-22 12:30:00', 0, '79ff4a90-631b-4a19-bbe7-6f4537798f7f', 128);
INSERT INTO public.reports_tokens VALUES (185, '2026-08-26 12:15:00', 0, '04de1ae1-b8ef-474f-a92d-cd42d9e347bf', 129);
INSERT INTO public.reports_tokens VALUES (186, '2026-08-27 11:00:00', 0, 'e19d56c6-e065-4b58-9736-6437d3c7ffa4', 130);
INSERT INTO public.reports_tokens VALUES (187, '2026-09-03 12:00:00', 0, '57618af3-9dc8-408f-b613-fad9af5617ea', 131);
INSERT INTO public.reports_tokens VALUES (188, '2026-09-05 13:00:00', 0, 'b4d5a8b7-fcc6-4819-a5f0-fe762c324ffc', 132);
INSERT INTO public.reports_tokens VALUES (189, '2026-09-04 14:30:00', 0, '14ce40e3-847c-43bb-a44c-131a3e612c0d', 133);
INSERT INTO public.reports_tokens VALUES (190, '2026-09-04 16:00:00', 0, '36bcb61a-69b8-4901-bf5c-26a8438a292e', 134);
INSERT INTO public.reports_tokens VALUES (191, '2026-09-07 14:00:00', 0, '39f36fb4-2c46-4769-9cf0-01930b6f2001', 135);
INSERT INTO public.reports_tokens VALUES (192, '2026-09-11 18:00:00', 0, '7fc2f270-ea79-420e-97d3-e78848b36247', 136);
INSERT INTO public.reports_tokens VALUES (193, '2026-09-17 11:30:00', 0, '3f9d0790-a29c-4b77-ab78-9f4ad23de19d', 137);
INSERT INTO public.reports_tokens VALUES (194, '2026-09-20 12:15:00', 0, 'e7679679-e62a-48fb-9869-30a64869db9d', 138);
INSERT INTO public.reports_tokens VALUES (195, '2026-09-26 13:00:00', 0, 'e8580cf9-93b9-4281-8698-26f1147a98a3', 139);
INSERT INTO public.reports_tokens VALUES (196, '2026-10-08 13:00:00', 0, 'b780deda-2d0a-469e-8173-028448e21f06', 140);
INSERT INTO public.reports_tokens VALUES (197, '2026-10-12 16:30:00', 0, '4185d18f-f399-48a2-85e0-d3e4172449ff', 141);
INSERT INTO public.reports_tokens VALUES (198, '2026-10-15 14:00:00', 0, 'e90376e1-155e-434a-b2c1-2d9a492c1ec0', 142);


--
-- Data for Name: staffers; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.staffers VALUES (39, 'laura.bianchi@example.com', 'Laura', '$2a$10$nsNTu.MSb7Mp72LD36jEBO0zO1CXIUx7AYw2XFNTlQLAdv0BYeDO2', 'Bianchi', 10, 'DOCTOR', true);
INSERT INTO public.staffers VALUES (40, 'giorgio.ferri@example.com', 'Giorgio', '$2a$10$2ViEHWZpgyaMG4R6Y90rC.RC3Hid5v07MvxgohIgrMbP4psjuHpZ.', 'Ferri', 11, 'DOCTOR', true);
INSERT INTO public.staffers VALUES (41, 'elena.marino@example.com', 'Elena', '$2a$10$T/0Pza6jM4T3TU5Vyn3Nwu5tJ4X8JCEW75vdJtAYx5lkJdn42IyXy', 'Marino', 11, 'DOCTOR', true);
INSERT INTO public.staffers VALUES (42, 'paolo.ricci@example.com', 'Paolo', '$2a$10$husGZB5HUtFWu8K2x0uanOap/LYtvAfPAVVeH9E2PqsWyDi4TiIZ2', 'Ricci', 12, 'DOCTOR', true);
INSERT INTO public.staffers VALUES (43, 'sara.colombo@example.com', 'Sara', '$2a$10$kxfXeD.unkW1V9ySanFHqeEUHeByizKf3VYSyy9pBlFhuvIu5RY/W', 'Colombo', 13, 'DOCTOR', true);
INSERT INTO public.staffers VALUES (1, 'chriespo@mail.it', 'Christian', '$2b$12$V16EPqTVjuH2bReqTqmqNeiABRmiwDXQTIc1w/1e8GA34yyBkTcli', 'Esposito', NULL, 'ADMIN', true);
INSERT INTO public.staffers VALUES (44, 'davide.greco@example.com', 'Davide', '$2a$10$AQQXoxCuVNf2z0mbw8niVushRqTX64J7wp8i54jCNCj3uCMhRA/d2', 'Greco', 13, 'DOCTOR', true);
INSERT INTO public.staffers VALUES (45, 'francesca.conti@example.com', 'Francesca', '$2a$10$NVZsLvsp4vk4vsmZ44HPuOtU/z5JqVvmgT9VrPfJknaq3Qg2AvF1W', 'Conti', 14, 'DOCTOR', true);
INSERT INTO public.staffers VALUES (46, 'matteo.galli@example.com', 'Matteo', '$2a$10$ihpQwUPCh3817Upo5BUdm.vzaJr8ndpsvjOpceoIxUfZKoB35syUS', 'Galli', 15, 'DOCTOR', true);
INSERT INTO public.staffers VALUES (2, 'chriespodoc@mail.it', 'Christian', '$2b$12$AO1yFT4exaloVPzxwZvZNOiOrWG2lnOfGjV3ZoFHLWIjMPcWtCRx6', 'Esposito', 10, 'DOCTOR', true);
INSERT INTO public.staffers VALUES (47, 'admin@example.com', 'Amministrazione', '$2a$10$i8v5yjSKtiLX29VUnXrxUeCEkcATMkv2JySQRm4effuF8pZvtLx3m', 'Demo', NULL, 'ADMIN', true);


--
-- Name: appointments_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.appointments_id_seq', 195, true);


--
-- Name: days_of_week_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.days_of_week_id_seq', 65, true);


--
-- Name: medical_examination_types_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.medical_examination_types_id_seq', 15, true);


--
-- Name: patients_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.patients_id_seq', 53, true);


--
-- Name: reports_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.reports_id_seq', 143, true);


--
-- Name: reports_tokens_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.reports_tokens_id_seq', 200, true);


--
-- Name: staffers_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.staffers_id_seq', 47, true);


--
-- PostgreSQL database dump complete
--

\unrestrict yOzgYU3wDsr8bFavNftWnFs6graspOBh5mbLqcLyw2ISWucAi49hh0miGhuwCM2

