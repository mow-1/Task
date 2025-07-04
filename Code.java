
import java.util.ArrayList;
import java.util.List;

class Product{
    String name;
    float price;
    int quantity;
    boolean MayExpire(){
        return false;
    }
    boolean MayShip(){
        return this instanceof Shipping;
    }
}
class Expire extends Product{
    int expireyDate;
    boolean MayExpire(int todaysdate){
        return todaysdate > expireyDate ;
    }
}
interface Shipping{
    float getweight();
    String getName();
}
class Ship extends Product implements Shipping {
    float weight; 
    public String getName(){
        return name;
    }
    public float getweight(){
        return weight;
    }
}
class Item {
	Product product;
	int quantity;
	Item(Product product, int quantity) {
	this.product = product;
	this.quantity= quantity;
	}
}
class Cart{
    List<Item> items= new ArrayList<>();
    void addProduct(Product product, int quantity) {
        if (quantity > product.quantity)
		    throw new RuntimeException("Item out of stock");
        items.add(new Item(product, quantity));
    }
    boolean isEmpty(){
        return items.isEmpty();
    }
    List<Item> getItems(){
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
class Customer {
	double balance;
	Cart cart;
void checkout (int todaysdate){
    float subtotal= 0;
    float shippingFee= 0;
    List<Shipping> toShip= new ArrayList<>();
    if (cart.isEmpty()) throw new RuntimeException("Cart is empty");
    for (Item item : cart.getItems()) {
        Product product = item.product;
        if (product instanceof Expire) {
            if (((Expire) product).MayExpire(todaysdate))
                throw new RuntimeException(product.name +"is expired");
        }
        if (item.quantity > product.quantity)
            throw new RuntimeException(product.name +"is out of stock");
        subtotal += product.price * item.quantity;
        if (product instanceof Shipping) {
            Shipping s = (Shipping) product;
            shippingFee += s.getweight() * 10;
            toShip.add(s);
        }
    }
    float total = subtotal + shippingFee;
    if (balance< total)
        throw new RuntimeException("Insufficient balance");
    balance -=total;
    for (Item item: cart.getItems())
        item.product.quantity -=item.quantity;
    if (!toShip.isEmpty())
        ShippingService.ship(toShip);
    System.out.printf("Subtotal: %f\nShipping Fee: %f\nPaid Amount: %f\nCurrent Balance: %f\n",
    subtotal, shippingFee, total, balance);
}
}
class ShippingService{
    public static void ship(List <Shipping> items){
        for (Shipping item :items){
		System.out.println("Shipping" +item.getName() +"weight:"+item.getweight());
		}
    }
}
class Main {
	public static void main(String[] args) {
	Expire cheese = new Expire();
	cheese.name ="Cheese";
	cheese.price= 5;
	cheese.quantity= 10;
	cheese.expireyDate= 20250704;
	Ship tv = new Ship();
	tv.name = "TV";
	tv.price = 1000;
	tv.quantity = 5;
	tv.weight = 15;
	Product scratchCard = new Product();
	scratchCard.name="Scratch Card";
	scratchCard.price=2;
	scratchCard.quantity=100;
	Customer customer = new Customer();
	customer.balance = 5000;
	customer.cart = new Cart();
	customer.cart.addProduct(cheese, 2);
	customer.cart.addProduct(tv, 3);
	customer.cart.addProduct(scratchCard, 1);
	customer.checkout(20250704);
	System.out.println("Done");
	}
}

