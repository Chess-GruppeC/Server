package at.aau.se2.chessLogic.board;

public class Move {

    Location from;
    Location to;

    public Move(Location from, Location to) {
        this.from = from;
        this.to = to;
    }

    public Location getFrom() {
        return from;
    }

    public void setFrom(Location from) {
        this.from = from;
    }

    public Location getTo() {
        return to;
    }

    public void setTo(Location to) {
        this.to = to;
    }

    public boolean compareMoves(Move move){
        if (this.getFrom()==move.getFrom()
            && this.getTo()==move.getTo()){
            return true;
        }
        return false;
    }

}
