public class Card {
    private int rank;
    private String suit;
    private boolean isFaceUp;

    public Card(int r, String s){
        rank = r;
        suit = s;
        isFaceUp = false;
    }

    public int getRank(){
        return rank;
    }

    public String getSuit() {
        return suit;
    }

    public boolean isRed(){
        return (suit.equals("h") || suit.equals("d"));
    }

    public boolean isFaceUp(){
        return isFaceUp;
    }

    public void turnUp(){
        isFaceUp = true;
    }

    public void turnDown(){
        isFaceUp = false;
    }

    public String getFileName(){
        if (!isFaceUp)
            return "cards/back.gif";
        else{
            if (rank > 1 && rank < 10){
                return "cards/" + rank + suit + ".gif";
            }
            String name = "";
            if (rank == 10){
                name = "t";
            }
            else if (rank == 11){
                name = "j";
            }
            else if (rank == 12){
                name = "q";
            }
            else if (rank == 13){
                name = "k";
            }
            else{
                name = "a";
            }
            return "cards/" + name + suit + ".gif";
        }
    }
}
