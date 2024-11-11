package Mockito.Restaurant_Order_System_mockito;

public class OrderService {
    private MenuService menuService;
    private PaymentService paymentService;

    public OrderService(MenuService menuService, PaymentService paymentService) {
        this.menuService = menuService;
        this.paymentService = paymentService;
    }

    public String placeOrder(Long menuItemId, int quantity, PaymentDetails paymentDetails) {
        if (!menuService.isItemAvailable(menuItemId, quantity)) {
            return "Item is out of stock.";
        }

        boolean paymentSuccess = paymentService.processPayment(paymentDetails);
        if (paymentSuccess) {
            return "Order placed successfully.";
        } else {
            return "Payment failed.";
        }
    }
}
