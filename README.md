quizzo-ete
==========

A project for a talk by Ken Rimple and David Turanski on modern Spring applications.

# Stories

## Sign up for quizzo

User hits the quizzo site and is not currently signed in. User is presented with a signup page. The page asks:
- desired nickname (required)
- email address (required)

### Actions

- The user clicks 'join' and is signed into a waiting area page
- The user types in the nickname field - each change triggers a check for existing users of that nickname and a message shows if the nickname is taken
- Once a valid email address entered, it is validated against other existing users in the quizzo and a message shows if the email is in use
- The join button is disabled until the username is valid AND the email address is valid and not in use by another nickname

## Show quizzo player page

The quizzo player page is displayed once the user signs in. This page has an area for the question, another area below for the potential answers, and a VOTE NOW button. It also has a score banner at the top of the page, with your score, the question number (out of the total #), and the top scorers.

### Actions

- If no quiz question is active, show a "Please wait" or some sort of silly graphic 
- If a quiz question is active, show the question and the potential answers
- Show a voting button for each answer letter (A, B, C, D, ...)
- Clicking the button depresses it
- Clicking Vote submits your answer
- Clicking "leave game" ends your session with the game and gives you your score

## Show quizzo manager page

The quizzo manager controls the speed of the game. His panel contains:

- A preview box showing what the current user sees
- A 'next question' box showing the next question itself and <n> possible answers
- A box showing the top n high scoring nicknames and their scores

### Actions

- Next Question (ends the current voting period, updates scores, and moves to the next question)
- End quiz (finishes the current quiz and tallies the final scores)

