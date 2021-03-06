# chess logic βοΈΒ - *draft*

starting point is ``Board`` class - represents pieces on the chess board & holds all related state/actions for status, movement, checking, special-move-bar-info, etc.

![board representation](https://miro.medium.com/max/812/1*nH4AerChS2uPEy4igMIshw.png)

board representation

## state π§ 

The ``Board`` class will hold state:

- **fields** - array containing 64 ``Field`` objects that contain ``Pieces``
- **turn** - which party is currently to making move (``WHITE`` / ``BLACK`` )
- **moves** - stack to push/pop moves
- **energy** - special-move-bar energy progress for each party (0-100)
- ...

## mutations πΊπ½

& provide mutations: 

- **validate** - validates a potential move
- **move** - pushes a new move on to the ``moves`` stack for the current party that has ``turn``
- **challenge** - challenges the latest move on ``moves`` stack for the party that has ``turn``.  if validation of this move is illegal, latest move will be popped from ``moves`` stack & party that had ``turn`` prior to this move will have ``energy`` reset.  if validation of this move was legal, the party that currently has ``turn`` will have ``energy`` reset.
- **getPotentialMoves** - gets all valid potential moves for a certain piece that stands on a certain field
- ...

## initialization of board π

- standard chess starting position of pieces in ``fields`` array
- ``turn`` assigned to white (dice roll will decide which gets white)
- ``moves`` empty stack
- ``energy`` set to 0 for both parties
- ...

