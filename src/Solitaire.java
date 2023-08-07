import java.util.*;

public class Solitaire
{
	public static void main(String[] args)
	{
		new Solitaire();
	}

	private Stack<Card> stock;
	private Stack<Card> waste;
	private Stack<Card>[] foundations;
	private Stack<Card>[] piles;
	private SolitaireDisplay display;

	public Solitaire()
	{
		foundations = new Stack[4];
		piles = new Stack[7];
		stock = new Stack<Card>();
		waste = new Stack<Card>();
		for (int i = 0; i < 4; i++){
			foundations[i] = new Stack<Card>();
		}
		for (int i = 0; i < 7; i++){
			piles[i] = new Stack<Card>();
		}
		createStock();
		deal();
		//INSERT CODE HERE

		display = new SolitaireDisplay(this);
	}

	//returns the card on top of the stock,
	//or null if the stock is empty
	public Card getStockCard()
	{
		if (stock.isEmpty()){
			return null;
		}
		return stock.peek();
	}

	//returns the card on top of the waste,
	//or null if the waste is empty
	public Card getWasteCard()
	{
		if (waste.isEmpty()){
			return null;
		}
		return waste.peek();
	}

	//precondition:  0 <= index < 4
	//postcondition: returns the card on top of the given
	//               foundation, or null if the foundation
	//               is empty
	public Card getFoundationCard(int index)
	{
		if (foundations[index].isEmpty()){
			return null;
		}
		return foundations[index].peek();
	}

	//precondition:  0 <= index < 7
	//postcondition: returns a reference to the given pile
	public Stack<Card> getPile(int index)
	{
		Stack<Card> temp = piles[index];
		return temp;
	}

	//called when the stock is clicked
	public void stockClicked()
	{
		if(!display.isWasteSelected() && !display.isPileSelected()){

			if (stock.isEmpty()){
				resetStock();
			}
			else{
				dealThreeCards();
			}
		}
	}

	//called when the waste is clicked
	public void wasteClicked()
	{
		if (!waste.isEmpty() && !display.isWasteSelected() && !display.isPileSelected()){
			display.selectWaste();
		}
		else if (display.isWasteSelected()){
			if(canAddToFoundation(waste.peek(), validFoundationIndex(waste.peek()))){
				foundations[validFoundationIndex(waste.peek())].push(waste.pop());
			}
			display.unselect();
		}
	}

	//precondition:  0 <= index < 4
	//called when given foundation is clicked
	public void foundationClicked(int index)
	{
		if(display.isWasteSelected()){
			if(canAddToFoundation(getWasteCard(), index)){
				foundations[index].push(waste.pop());
				display.unselect();
			}
		}
		else if(display.isPileSelected()){
			if(canAddToFoundation(getPile(display.selectedPile()).peek(), index)){
				foundations[index].push(getPile(display.selectedPile()).pop());
				display.unselect();
			}
		}
		else if(display.isFoundationSelected()){
			display.unselect();
		}
		else{
			display.selectFoundation(index);
		}
	}

	//precondition:  0 <= index < 7
	//called when given pile is clicked
	public void pileClicked(int index)
	{
		if(display.isWasteSelected()){
			if(canAddToPile(waste.peek(), index)){
				piles[index].push(waste.pop());
			}
			display.unselect();
		}
		else if(display.isPileSelected() && display.selectedPile() != index){
			Stack<Card> temp = removeFaceUpCards(display.selectedPile());
			if(!temp.isEmpty() && canAddToPile(temp.peek(), index)){
				addToPile(temp, index);
			}
			else{
				addToPile(temp, display.selectedPile());
			}
			display.unselect();
		}
		else if(display.isPileSelected() && display.selectedPile() == index){
			if(!getPile(index).isEmpty() && canAddToFoundation(getPile(index).peek(),
					validFoundationIndex(getPile(index).peek()))){
				foundations[validFoundationIndex(getPile(index).peek())].push(piles[index].pop());
			}
			else{
				display.unselect();
			}
		}
		else if(display.isFoundationSelected()){
			if(canAddToPile(getFoundationCard(display.selectedFoundation()), index)){
				piles[index].push(foundations[display.selectedFoundation()].pop());
			}
			display.unselect();
		}
		else if(!piles[index].isEmpty() && piles[index].peek().isFaceUp()){
			display.selectPile(index);
		}
		else if(!piles[index].peek().isFaceUp()){
			piles[index].peek().turnUp();
		}
	}

	public void createStock()
	{
		ArrayList<Card> deck = new ArrayList<Card>();
		for(int i = 1; i <= 13; i++){
			deck.add(new Card(i, "s"));
			deck.add(new Card(i, "c"));
			deck.add(new Card(i, "h"));
			deck.add(new Card(i, "d"));
		}
		for(int i = 0; i < 52; i++){
			int index = (int)(Math.random()*deck.size());
			stock.push(deck.remove(index));
		}
	}

	public void deal()
	{
		for (int i = 0; i < 7; i++){
			for(int j = 0; j <= i; j++) {
				piles[i].push(stock.pop());
			}
			getPile(i).peek().turnUp();
		}
	}

	public void dealThreeCards()
	{
		if(stock.size() < 3){
			while(!stock.isEmpty()){
				waste.push(stock.pop());
				waste.peek().turnUp();
			}
		}
		else{
			for (int i = 0; i < 3; i++){
				waste.push(stock.pop());
				waste.peek().turnUp();
			}
		}
	}

	public void resetStock(){
		while(!waste.isEmpty()){
			stock.push(waste.pop());
			stock.peek().turnDown();
		}
	}

	public boolean canAddToPile(Card card, int index)
	{
		if (piles[index].isEmpty()){
			if(card.isFaceUp() && card.getRank() == 13){
				return true;
			}
			return false;
		}
		Card temp = piles[index].peek();
		if(card.isFaceUp() && temp.isFaceUp() && temp.getRank() == card.getRank() + 1
				&& temp.isRed() == !card.isRed()){
			return true;
		}
		return false;
	}

	private Stack<Card> removeFaceUpCards(int index)
	{
		Stack<Card> temp = new Stack<Card>();
		while(!piles[index].isEmpty() && piles[index].peek().isFaceUp()){
			temp.push(piles[index].pop());
		}
		return temp;
	}

	private void addToPile(Stack<Card> cards, int index)
	{
		while(!cards.isEmpty()){
			piles[index].push(cards.pop());
		}
	}

	private boolean canAddToFoundation(Card card, int index)
	{
		if(foundations[index].isEmpty()){
			if(card.isFaceUp() && card.getRank() == 1){
				return true;
			}
		}
		else if(card.isFaceUp() && getFoundationCard(index).getSuit().equals(card.getSuit()) &&
				getFoundationCard(index).getRank() == card.getRank() - 1){
			return true;
		}
		return false;
	}

	private int emptyFoundations(){
		for (int i = 0; i < 4; i++){
			if(foundations[i].isEmpty()){
				return i;
			}
		}
		return -1;
	}

	private int validFoundationIndex(Card card){
		for (int i = 0; i < 4; i++){
			if(!foundations[i].isEmpty() && getFoundationCard(i).getSuit().equals(card.getSuit())){
				return i;
			}
		}
		return emptyFoundations();
	}
}