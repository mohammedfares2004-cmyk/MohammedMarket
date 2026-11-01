package mohammedmarket;

 import java.util.ArrayList;

// كلاس بنوعين (Multiple type parameters)
class LabeledValue<A, B> {
    private A label;
    private B value;

    public LabeledValue(A label, B value) {
        this.label = label;
        this.value = value;
    }

    public A getLabel() { return label; }
    public B getValue() { return value; }
}

// البضاعة: الكمية Q إما Integer (بالحبة) أو Double (بالكيلو)
class MarketGoods<Q extends Number> {
    private String title;
    private double unitPrice;
    private Q amount;

    public MarketGoods(String title, double unitPrice, Q amount) {
        this.title = title;
        this.unitPrice = unitPrice;
        this.amount = amount;
    }

    public String getTitle() { return title; }
    public double getUnitPrice() { return unitPrice; }
    public Q getAmount() { return amount; }

    public double totalWorth() {
        return unitPrice * amount.doubleValue();
    }
}

// الدكان
class MarketStall<Q extends Number> {
    protected String stallName;
    protected ArrayList<MarketGoods<Q>> goodsList = new ArrayList<MarketGoods<Q>>();

    public MarketStall(String stallName) {
        this.stallName = stallName;
    }

    public String getStallName() { return stallName; }
    public ArrayList<MarketGoods<Q>> getGoodsList() { return goodsList; }
    public void addGoods(MarketGoods<Q> g) { goodsList.add(g); }

    public double stockValue() {
        double total = 0;
        for (MarketGoods<Q> g : goodsList) {
            total += g.totalWorth();
        }
        return total;
    }

    // اسم الدكان وعدد بضائعه (Autoboxing: int -> Integer)
    public LabeledValue<String, Integer> summary() {
        return new LabeledValue<String, Integer>(stallName, goodsList.size());
    }
}

// generic يرث generic
class BranchStall<Q extends Number> extends MarketStall<Q> {
    private String area;

    public BranchStall(String stallName, String area) {
        super(stallName);
        this.area = area;
    }

    public String getArea() { return area; }
}

// غير generic يرث generic: عربة الفواكه تبيع بالكيلو دائماً
class KiloFruitCart extends MarketStall<Double> {
    public KiloFruitCart() {
        super("Fruit Cart");
    }
}

public class MohammedMarket {

    // Wildcard: تعمل لأي دكان (بالحبة أو بالكيلو)
    public static void printStallReport(MarketStall<? extends Number> s) {
        System.out.println(s.getStallName() + " stock value: " + s.stockValue());
    }

    // Generic method: أغلى بضاعة بنفس نوع الدكان
    public static <Q extends Number> MarketGoods<Q> priciestGoods(MarketStall<Q> s) {
        MarketGoods<Q> best = s.getGoodsList().get(0);
        for (MarketGoods<Q> g : s.getGoodsList()) {
            if (g.getUnitPrice() > best.getUnitPrice()) {
                best = g;
            }
        }
        return best;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void main(String[] args) {

        // الكميات بالكيلو (Double)
        KiloFruitCart cart = new KiloFruitCart();
        cart.addGoods(new MarketGoods<Double>("Mango", 6.0, 18.5));
        cart.addGoods(new MarketGoods<Double>("Dates", 12.0, 25.0));

        // الكميات أعداد صحيحة (Integer)
        MarketStall<Integer> bakery = new MarketStall<Integer>("Morning Bakery");
        bakery.addGoods(new MarketGoods<Integer>("Samoli Bread", 0.25, 80));

        BranchStall<Integer> juiceBar = new BranchStall<Integer>("Juice Corner", "Old Market");
        juiceBar.addGoods(new MarketGoods<Integer>("Orange Juice", 1.5, 70));

        printStallReport(cart);
        printStallReport(bakery);
        printStallReport(juiceBar);
        System.out.println("Juice Corner area: " + juiceBar.getArea());
        System.out.println("Priciest on the cart: " + priciestGoods(cart).getTitle());

        // Unboxing: Integer -> int
        LabeledValue<String, Integer> info = cart.summary();
        int count = info.getValue();
        System.out.println(info.getLabel() + " has " + count + " goods");

        // Raw type: بدون تحديد النوع، ويلزم Casting
        MarketGoods raw = new MarketGoods("Sample", 1.0, 5);
        Number q = (Number) raw.getAmount();
        System.out.println("Raw type amount: " + q.intValue());

     
    }
}