package Mockito.Restaurant_Order_System_mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class OrderServiceTest {

    private OrderService orderService;
    private MenuService menuService;
    private PaymentService paymentService;

    @Before
    public void setUp() {
        menuService = mock(MenuService.class);
        paymentService = mock(PaymentService.class);
        orderService = new OrderService(menuService, paymentService);
    }

    @Test
    public void testPlaceOrder_whenItemIsAvailable_andPaymentSucceeds() {
        Long menuItemId = 1L;
        int quantity = 2;
        PaymentDetails paymentDetails = new PaymentDetails("1234567890123456", 20.0);

        // Mocking MenuService to return true (item available)
        when(menuService.isItemAvailable(menuItemId, quantity)).thenReturn(true);

        // Mocking PaymentService to return true (payment successful)
        when(paymentService.processPayment(paymentDetails)).thenReturn(true);

        // Act
        String result = orderService.placeOrder(menuItemId, quantity, paymentDetails);

        // Assert
        assertEquals("Order placed successfully.", result);
        verify(menuService, times(1)).isItemAvailable(menuItemId, quantity);
        verify(paymentService, times(1)).processPayment(paymentDetails);
    }

    @Test
    public void testPlaceOrder_whenItemIsOutOfStock() {
        Long menuItemId = 1L;
        int quantity = 5;
        PaymentDetails paymentDetails = new PaymentDetails("1234567890123456", 50.0);

        // Mocking MenuService to return false (item out of stock)
        when(menuService.isItemAvailable(menuItemId, quantity)).thenReturn(false);

        // Act
        String result = orderService.placeOrder(menuItemId, quantity, paymentDetails);

        // Assert
        assertEquals("Item is out of stock.", result);
        verify(menuService, times(1)).isItemAvailable(menuItemId, quantity);
        verify(paymentService, times(0)).processPayment(paymentDetails); // Payment should not be processed
    }

    @Test
    public void testPlaceOrder_whenPaymentFails() {
        Long menuItemId = 1L;
        int quantity = 2;
        PaymentDetails paymentDetails = new PaymentDetails();

        // Mocking MenuService to return true (item available)
        when(menuService.isItemAvailable(menuItemId, quantity)).thenReturn(true);

        // Mocking PaymentService to return false (payment failed)
        when(paymentService.processPayment(paymentDetails)).thenReturn(false);

        // Act
        String result = orderService.placeOrder(menuItemId, quantity, paymentDetails);

        // Assert
        assertEquals("Payment failed.", result);
        verify(menuService, times(1)).isItemAvailable(menuItemId, quantity);
        verify(paymentService, times(1)).processPayment(paymentDetails);
    }
}

