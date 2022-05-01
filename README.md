# DotaPickHelper
A software to assist player during the pick process of DotA 2 game.
Application idea is easy and there are some realisations as browser applications already, but I don't like them so made another one myself).
________________________________________________________________________________________________________________________________
As picking progresses user can type anything and application will react with a big portrait in the center of a screen. This is a porttrait of a hero the application thinks user meant by the imput he created. Once user sees the right hero in the center he can press Enter to update pick (application manages the right pick order itself).
______________________________________________________________________
On the right and on the left there are 10 rows of heroes. One row symbolizes the best picks for a role (pos1, pos2 etc...) for a team, doubled.

Suggested heroes feat "counterpick" logic: application counts the candidates with the highes advantage to counter the enemy set of heroes.
______________________________________________________________________
The data about counters contained in 123 tables (one per hero) with 122 lines in them - each hero pair (hero1-hero2 pair != hero2-hero1 pair).
Right now, data updating mechanism is parsing 123 hero advantage pages from dotabuff.com (requests the latest patch). On roughly 80 kb/s internet, updating process takes about 100-120 seconds. At the moment, user can't call the update method from UI, it only possible through uncommenting the source code (application still in progress tho).
____________________________________________________________________________
![изображение](https://user-images.githubusercontent.com/25298003/166165066-d676c0a1-0656-47ce-935a-b2f9e57017c0.png)
