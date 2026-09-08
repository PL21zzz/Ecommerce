package com.ai.ecommerce.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ai.ecommerce.config.VNPayConfig;
import com.ai.ecommerce.dto.CreatePaymentRequest;
import com.ai.ecommerce.dto.OrderItemRequest;
import com.ai.ecommerce.model.Order;
import com.ai.ecommerce.model.OrderItem;
import com.ai.ecommerce.repository.OrderRepository;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/vnpay")
public class VNPayController {

    @Value("${vnp.payUrl}") private String vnpPayUrl;
    @Value("${vnp.returnUrl}") private String vnpReturnUrl;
    @Value("${vnp.tmnCode}") private String vnpTmnCode;
    @Value("${vnp.hashSecret}") private String vnpHashSecret;

    @Autowired private OrderRepository orderRepository;

    @PostMapping("/create-cod-order")
    public ResponseEntity<?> createCodOrder(@RequestBody CreatePaymentRequest request) {
        try {
            double deliveryFee = request.getDeliveryFee() != null ? request.getDeliveryFee() : 0.0;
            double subtotal = request.getItems() == null ? 0.0 : request.getItems().stream()
                    .mapToDouble(item -> {
                        double price = item.getUnitPrice() != null ? item.getUnitPrice() : 0.0;
                        int quantity = item.getQuantity() != null ? item.getQuantity() : 0;
                        return price * quantity;
                    })
                    .sum();
            double requestedTotal = request.getTotalAmount() != null ? request.getTotalAmount() : 0.0;
            double totalAmount = subtotal > 0 ? subtotal + deliveryFee : requestedTotal;

            String orderCode = "COD-" + System.currentTimeMillis();
            Order order = buildPendingOrder(orderCode, totalAmount, deliveryFee, request);
            order.setStatus("COD");
            orderRepository.save(order);

            return ResponseEntity.ok(Map.of("message", "Order placed successfully", "orderCode", orderCode));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/create-vietqr-order")
    public ResponseEntity<?> createVietQrOrder(@RequestBody CreatePaymentRequest request) {
        try {
            double deliveryFee = request.getDeliveryFee() != null ? request.getDeliveryFee() : 0.0;
            double subtotal = request.getItems() == null ? 0.0 : request.getItems().stream()
                    .mapToDouble(item -> {
                        double price = item.getUnitPrice() != null ? item.getUnitPrice() : 0.0;
                        int quantity = item.getQuantity() != null ? item.getQuantity() : 0;
                        return price * quantity;
                    })
                    .sum();
            double requestedTotal = request.getTotalAmount() != null ? request.getTotalAmount() : 0.0;
            double totalAmount = subtotal > 0 ? subtotal + deliveryFee : requestedTotal;

            String orderCode = "QR-" + System.currentTimeMillis();
            Order order = buildPendingOrder(orderCode, totalAmount, deliveryFee, request);
            order.setStatus("PAID_QR");
            orderRepository.save(order);

            long amountVnd = Math.round(totalAmount * 25000);
            return ResponseEntity.ok(Map.of(
                "message", "VietQR order created successfully",
                "orderCode", orderCode,
                "amountVnd", String.valueOf(amountVnd)
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/create-payment")
    public ResponseEntity<?> createPayment(@RequestBody CreatePaymentRequest request) {
        try {
            double deliveryFee = request.getDeliveryFee() != null ? request.getDeliveryFee() : 0.0;
            double subtotal = request.getItems() == null ? 0.0 : request.getItems().stream()
                    .mapToDouble(item -> {
                        double price = item.getUnitPrice() != null ? item.getUnitPrice() : 0.0;
                        int quantity = item.getQuantity() != null ? item.getQuantity() : 0;
                        return price * quantity;
                    })
                    .sum();
            double requestedTotal = request.getTotalAmount() != null ? request.getTotalAmount() : 0.0;
            double totalAmount = subtotal > 0 ? subtotal + deliveryFee : requestedTotal;

            String orderCode = "COFFEE-" + System.currentTimeMillis();
            Order order = buildPendingOrder(orderCode, totalAmount, deliveryFee, request);
            orderRepository.save(order);

            String paymentUrl = buildPaymentUrl(orderCode, totalAmount);
            return ResponseEntity.ok(Map.of("url", paymentUrl, "orderCode", orderCode));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/payment-callback")
    public void paymentCallback(
            @RequestParam Map<String, String> queryParams,
            HttpServletResponse response
    ) throws IOException {
        String responseCode = queryParams.get("vnp_ResponseCode");
        String orderCode = queryParams.get("vnp_TxnRef");

        Optional<Order> orderOpt = orderRepository.findByOrderCode(orderCode);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus("00".equals(responseCode) ? "PAID" : "FAILED");
            orderRepository.save(order);
        }

        response.setContentType("text/html");
        response.getWriter().write("<html><body><script>window.close();</script><h3>Payment Processed! You can return to the app.</h3></body></html>");
    }

    @GetMapping("/orders")
    public List<Order> getOrders(@RequestParam(required = false) Long userId) {
        List<Order> orders = userId != null
                ? orderRepository.findByUserIdOrderByCreatedAtDesc(userId)
                : orderRepository.findAllByOrderByCreatedAtDesc();
        expireStalePendingOrders(orders);
        return orders.stream()
                .filter(order -> !"PENDING".equals(order.getStatus()))
                .collect(Collectors.toList());
    }

    @GetMapping("/all-orders")
    public ResponseEntity<?> getAllOrdersLegacy() {
        return ResponseEntity.ok(orderRepository.findAll());
    }

    private Order buildPendingOrder(
            String orderCode,
            double totalAmount,
            double deliveryFee,
            CreatePaymentRequest request
    ) {
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setOrderCode(orderCode);
        order.setTotalAmount(totalAmount);
        order.setDeliveryFee(deliveryFee);
        order.setCustomerName(request.getCustomerName());
        order.setPhone(request.getPhone());
        order.setAddress(request.getAddress());
        order.setNote(request.getNote());
        order.setStatus("PENDING");

        if (request.getItems() != null) {
            for (OrderItemRequest itemRequest : request.getItems()) {
                OrderItem item = new OrderItem();
                item.setProductId(itemRequest.getProductId());
                item.setProductTitle(itemRequest.getProductTitle());
                item.setImage(itemRequest.getImage());
                item.setSize(itemRequest.getSize());
                item.setUnitPrice(itemRequest.getUnitPrice());
                item.setQuantity(itemRequest.getQuantity());
                item.setOrder(order);
                order.getItems().add(item);
            }
        }

        return order;
    }

    private String buildPaymentUrl(String orderCode, double dollarAmount) throws Exception {
        long vnAmount = Math.round(dollarAmount * 25000);

        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", vnpTmnCode);
        vnpParams.put("vnp_Amount", String.valueOf(vnAmount * 100));
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", orderCode);
        vnpParams.put("vnp_OrderInfo", "Thanh toan don hang " + orderCode);
        vnpParams.put("vnp_OrderType", "other");
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", vnpReturnUrl);
        vnpParams.put("vnp_IpAddr", "127.0.0.1");

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        vnpParams.put("vnp_CreateDate", formatter.format(calendar.getTime()));

        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> iterator = fieldNames.iterator();

        while (iterator.hasNext()) {
            String fieldName = iterator.next();
            String fieldValue = vnpParams.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                String encodedFieldName = URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString());
                String encodedFieldValue = URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString());

                query.append(encodedFieldName).append('=').append(encodedFieldValue);
                hashData.append(fieldName).append('=').append(encodedFieldValue);

                if (iterator.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String secureHash = VNPayConfig.hmacSHA512(vnpHashSecret, hashData.toString());
        return vnpPayUrl + "?" + query + "&vnp_SecureHash=" + secureHash;
    }

    private void expireStalePendingOrders(List<Order> orders) {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(30);
        List<Order> staleOrders = orders.stream()
                .filter(order -> "PENDING".equals(order.getStatus()))
                .filter(order -> order.getCreatedAt() != null && order.getCreatedAt().isBefore(cutoff))
                .collect(Collectors.toList());

        for (Order order : staleOrders) {
            order.setStatus("FAILED");
        }

        if (!staleOrders.isEmpty()) {
            orderRepository.saveAll(staleOrders);
        }
    }
}
