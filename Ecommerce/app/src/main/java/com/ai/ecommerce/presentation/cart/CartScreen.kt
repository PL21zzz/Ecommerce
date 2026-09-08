package com.ai.ecommerce.presentation.cart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ai.ecommerce.presentation.auth.AuthUser
import com.ai.ecommerce.presentation.cart.components.CartItemRow
import com.ai.ecommerce.presentation.cart.components.EmptyCart
import com.ai.ecommerce.presentation.cart.components.OrderSummarySection
import com.ai.ecommerce.ui.theme.BackgroundLight
import com.ai.ecommerce.ui.theme.BorderColor
import com.ai.ecommerce.ui.theme.CoffeeOrange
import com.ai.ecommerce.ui.theme.SurfaceLight
import com.ai.ecommerce.ui.theme.TextPrimary
import com.ai.ecommerce.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun CartScreen(
    viewModel: CartViewModel,
    currentUser: AuthUser,
    onBackClick: () -> Unit,
    onGoShoppingClick: () -> Unit
) {
    val isCartEmpty = viewModel.cartItems.isEmpty()
    var isOpeningWebView by remember { mutableStateOf(false) }
    var paymentUrl by remember { mutableStateOf("") }
    var checkoutError by remember { mutableStateOf<String?>(null) }
    var isCheckingOut by remember { mutableStateOf(false) }

    // Phương thức thanh toán được chọn (Mặc định: VietQR)
    var selectedMethod by remember { mutableStateOf(PaymentMethod.VIET_QR) }

    var customerName by remember { mutableStateOf(currentUser.name) }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    // State cho Dialog VietQR
    var showVietQrDialog by remember { mutableStateOf(false) }
    var qrOrderCode by remember { mutableStateOf("") }
    var qrAmountVnd by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()
    val canCheckout = customerName.isNotBlank() && phone.isNotBlank() && address.isNotBlank()

    if (isOpeningWebView && paymentUrl.isNotEmpty()) {
        PaymentWebViewScreen(url = paymentUrl) { isSuccess ->
            isOpeningWebView = false
            if (isSuccess) {
                viewModel.clearCart()
                onGoShoppingClick()
            } else {
                checkoutError = "Payment was not completed. Your cart is still here."
            }
        }
        return
    }

    // Dialog VietQR Chuyển Khoản Ngân Hàng / ZaloPay / MoMo
    if (showVietQrDialog) {
        val vietQrImageUrl = "https://img.vietqr.io/image/MB-0348751577-compact2.png?amount=$qrAmountVnd&addInfo=$qrOrderCode&accountName=PHONG%20COFFEE"

        AlertDialog(
            onDismissRequest = { showVietQrDialog = false },
            title = {
                Text(
                    text = "Quét Mã VietQR Thanh Toán",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextPrimary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Dùng ứng dụng Ngân hàng / ZaloPay / MoMo để quét mã:", fontSize = 13.sp, color = TextSecondary, textAlign = TextAlign.Center)

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        AsyncImage(
                            model = vietQrImageUrl,
                            contentDescription = "VietQR Code",
                            modifier = Modifier.size(240.dp).padding(8.dp),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Text(text = "Số tiền: ${String.format("%,d", qrAmountVnd.toLongOrNull() ?: 0)} VNĐ", fontWeight = FontWeight.Bold, color = CoffeeOrange, fontSize = 16.sp)
                    Text(text = "Nội dung: $qrOrderCode", fontSize = 12.sp, color = TextPrimary)
                    Text(text = "Ngân hàng: MB Bank - STK: 0348751577", fontSize = 12.sp, color = TextSecondary)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showVietQrDialog = false
                        viewModel.clearCart()
                        onGoShoppingClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("✅ Tôi Đã Chuyển Khoản Thành Công", fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showVietQrDialog = false }, modifier = Modifier.fillMaxWidth()) {
                    Text("Đóng", color = TextSecondary)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BackgroundLight)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.ArrowBackIosNew, "Back", Modifier.clickable { onBackClick() }, tint = TextPrimary)
                Text("Cart", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)

                if (!isCartEmpty) {
                    Icon(Icons.Default.DeleteOutline, "Clear", Modifier.clickable { viewModel.clearCart() }, tint = TextPrimary)
                } else {
                    Spacer(modifier = Modifier.size(24.dp))
                }
            }
        },
        bottomBar = {
            if (!isCartEmpty) {
                Box(
                    modifier = Modifier
                        .background(BackgroundLight)
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Button(
                        onClick = {
                            checkoutError = null
                            if (!canCheckout) {
                                checkoutError = "Please enter your name, phone, and delivery address."
                            } else {
                                isCheckingOut = true
                                coroutineScope.launch {
                                    when (selectedMethod) {
                                        PaymentMethod.VIET_QR -> {
                                            val result = viewModel.createVietQrOrder(
                                                currentUser = currentUser,
                                                customerName = customerName,
                                                phone = phone,
                                                address = address,
                                                note = note
                                            )
                                            result.onSuccess { data ->
                                                qrOrderCode = data["orderCode"].orEmpty()
                                                qrAmountVnd = data["amountVnd"].orEmpty()
                                                showVietQrDialog = true
                                            }.onFailure { exception ->
                                                checkoutError = exception.localizedMessage ?: "VietQR Order failed."
                                            }
                                        }
                                        PaymentMethod.COD -> {
                                            val result = viewModel.createCodOrder(
                                                currentUser = currentUser,
                                                customerName = customerName,
                                                phone = phone,
                                                address = address,
                                                note = note
                                            )
                                            result.onSuccess {
                                                viewModel.clearCart()
                                                onGoShoppingClick()
                                            }.onFailure { exception ->
                                                checkoutError = exception.localizedMessage ?: "COD Order failed."
                                            }
                                        }
                                        PaymentMethod.VNPAY -> {
                                            val result = viewModel.createPaymentUrl(
                                                currentUser = currentUser,
                                                customerName = customerName,
                                                phone = phone,
                                                address = address,
                                                note = note
                                            )
                                            result.onSuccess { url ->
                                                paymentUrl = url
                                                isOpeningWebView = true
                                            }.onFailure { exception ->
                                                checkoutError = exception.localizedMessage ?: "Checkout failed."
                                            }
                                        }
                                    }
                                    isCheckingOut = false
                                }
                            }
                        },
                        enabled = !isCheckingOut,
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when (selectedMethod) {
                                PaymentMethod.VIET_QR -> Color(0xFF673AB7)
                                PaymentMethod.COD -> Color(0xFF4CAF50)
                                PaymentMethod.VNPAY -> CoffeeOrange
                            }
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = if (isCheckingOut) "Processing..." else "Place Order ($${String.format("%.2f", viewModel.total)})",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        },
        containerColor = BackgroundLight
    ) { padding ->
        if (isCartEmpty) {
            EmptyCart(
                onGoShoppingClick = onGoShoppingClick,
                modifier = Modifier.padding(padding)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                checkoutError?.let { message ->
                    item {
                        Text(message, color = Color(0xFFED5151), fontSize = 14.sp)
                    }
                }

                // 1. Thông tin giao hàng
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Delivery Details", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        OutlinedTextField(
                            value = customerName,
                            onValueChange = { customerName = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            label = { Text("Name") }
                        )
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            label = { Text("Phone") }
                        )
                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Address") }
                        )
                        OutlinedTextField(
                            value = note,
                            onValueChange = { note = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Note") }
                        )
                    }
                }

                // 2. Chọn Phương Thức Thanh Toán (Payment Method Selector UI)
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Payment Method", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)

                        // Option 1: VietQR
                        PaymentMethodOptionCard(
                            title = "📱 VietQR / Banking / ZaloPay",
                            subtitle = "Quét mã QR chuyển khoản nhanh",
                            isSelected = selectedMethod == PaymentMethod.VIET_QR,
                            selectedColor = Color(0xFF673AB7),
                            onClick = { selectedMethod = PaymentMethod.VIET_QR }
                        )

                        // Option 2: COD
                        PaymentMethodOptionCard(
                            title = "💵 Cash on Delivery (COD)",
                            subtitle = "Thanh toán tiền mặt khi nhận cà phê",
                            isSelected = selectedMethod == PaymentMethod.COD,
                            selectedColor = Color(0xFF4CAF50),
                            onClick = { selectedMethod = PaymentMethod.COD }
                        )

                        // Option 3: VNPay Online Sandbox
                        PaymentMethodOptionCard(
                            title = "💳 VNPay Sandbox",
                            subtitle = "Thanh toán qua ví / thẻ ngân hàng VNPay",
                            isSelected = selectedMethod == PaymentMethod.VNPAY,
                            selectedColor = CoffeeOrange,
                            onClick = { selectedMethod = PaymentMethod.VNPAY }
                        )
                    }
                }

                // 3. Danh sách món trong giỏ hàng
                items(viewModel.cartItems) { item ->
                    CartItemRow(
                        item = item,
                        onIncrease = { viewModel.updateQuantity(item.product.id, item.size, 1) },
                        onDecrease = { viewModel.updateQuantity(item.product.id, item.size, -1) }
                    )
                }

                // 4. Tổng kết giá tiền
                item {
                    OrderSummarySection(
                        subtotal = viewModel.subtotal,
                        deliveryFee = viewModel.deliveryFee,
                        total = viewModel.total
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentMethodOptionCard(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    selectedColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceLight),
        border = if (isSelected) BorderStroke(2.dp, selectedColor) else BorderStroke(1.dp, BorderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = subtitle, fontSize = 12.sp, color = TextSecondary)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Outlined.Circle,
                contentDescription = null,
                tint = if (isSelected) selectedColor else TextSecondary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
