# SortingHat
Randomly sorts a list

So this is barely tested and I'm sure you'll find bugs.
 
v2:

- For now writing to a csv overwrites it rather than adding new lines
- The program will complain if it finds several csv files in the input or output and you've answered 'y' to using a csv (unless a list.csv exists, in which case it'll be used)
- Adding a specific path has not been tested
- if you use "\" (as windows likes) in a specific path, things will probably go wrong
- the help function is very unhelpful

- On the plus side you can quit at any time by pressing 'q' ^^

v3:

- CSV's should no longer get overwritten
- When seeing several files in a folder, SH now presents you with a choice between them (unless one is 'list.csv'. I might remove that)
- Help is helpful now
- You are now asked to confirm if SH picked a default option for you
- Made things prettier
- Restructured the code (but you won't be able to tell)
- \ and specific paths still not tested :p
- Added an external dependency which multiplied program size by ~100 which seems a bit excessive

v4: 

- You can now set options by modifying the Options.txt file
- command 'o' added which displays option values, and lets you set them for the current run of the program
- MAX_REPEATS option added which ensures that a certain list element will never be repeated more than the specified number of times. 
(ex: "MAX_REPEATS: 2" forbids 1,1,1 from appearing in the shuffled list) 

v4.1:

- Fixed some bugs

v4.2:

- MAX_REPEATS can be UNSET to remove constraints
- Unrecognised responses from the user are better handled