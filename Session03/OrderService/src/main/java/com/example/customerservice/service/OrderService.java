package com.example.customerservice.service;

import com.example.customerservice.exception.CustomException;
import com.example.customerservice.model.dto.request.OrderRequestDTO;
import com.example.customerservice.model.dto.response.ApiResponseError;
import com.example.customerservice.model.dto.response.Customer;
import com.example.customerservice.model.dto.response.CustomerResponse;
import com.example.customerservice.model.dto.response.Product;
import com.example.customerservice.model.entity.Order;
import com.example.customerservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service

public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RestTemplate restTemplate ;
    @Autowired
    private DiscoveryClient discoveryClient ;

    public ResponseEntity<Product> getProductFromProductService(String productId) {

        // BƯỚC A: Hỏi Eureka lấy danh sách các máy chủ đang chạy PRODUCT-SERVICE
        // Lưu ý: Tên service (serviceId) thường là spring.application.name viết hoa
        List<ServiceInstance> instances = discoveryClient.getInstances("ProductService");

        // BƯỚC B: Xử lý lỗi (Theo yêu cầu bài tập: Trả về 503 nếu rỗng)
        if (instances == null || instances.isEmpty()) {
            // Bạn có thể throw ra một Exception tuỳ chỉnh, ở đây dùng tạm ResponseStatusException
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "PRODUCT-SERVICE hiện không khả dụng (Không tìm thấy instance nào)"
            );
        }

        // BƯỚC C: Lấy Instance đầu tiên trong danh sách (Tương lai bạn có thể viết logic random để cân bằng tải)
        ServiceInstance productInstance = instances.get(0);

        // BƯỚC D: Lấy URL gốc (Ví dụ: http://localhost:9090) từ instance
        String baseUrl = productInstance.getUri().toString();

        // BƯỚC E: Nối chuỗi để tạo ra API URL hoàn chỉnh
        // Ví dụ: http://localhost:9090/api/products/123
        String targetUrl = baseUrl + "/api/v1/products/" + productId;

        // BƯỚC F: Thực hiện gọi API lấy về sản phẩm
         Product product = restTemplate.getForObject(targetUrl, Product.class);

        return new ResponseEntity<>(product,HttpStatus.OK);
    }

    public ResponseEntity<?> getProductFromProductService2(String productId) {
        try {
            String targetUrl = "http://ProductService/api/v1/products/" + productId;

            // BƯỚC F: Thực hiện gọi API lấy về sản phẩm
            Product product = restTemplate.getForObject(targetUrl, Product.class);
            return new ResponseEntity<>(product,HttpStatus.OK);
        } catch (Exception e) {
            ApiResponseError apiResponseError = ApiResponseError
                    .builder()
                    .status(503)
                    .error("Lỗi hệ thống")
                    .message("Hệ thống đang quá tải, yêu cầu của bạn đã được ghi nhận nhưng chưa thể hoàn tất kiểm tra kho. Vui lòng quay lại sau 1 phút.")
                    .timestamp(LocalDateTime.now())
                    .build();
            return new ResponseEntity<>(apiResponseError,HttpStatus.SERVICE_UNAVAILABLE);
        }


    }
    public Customer findCustomerById(Long customerId) {
        try {
            String urlCustomerService = "http://CustomerService/api/v1/auth/"+ customerId;
            CustomerResponse customerResponse = restTemplate.getForObject(urlCustomerService,CustomerResponse.class);
            assert customerResponse != null;
            return Customer
                    .builder()
                    .id(customerResponse.getId())
                    .email(customerResponse.getEmail())
                    .fullName(customerResponse.getFullName())
                    .build();
        }catch (Exception ex){
            throw new CustomException("Có lỗi khi call api tới customer service hoặc customer không tồn tại");
        }
    }

    public Product findProductById(Long productId) {
        try {
            String urlProductService = "http://ProductService/api/v1/products/"+ productId;
            return restTemplate.getForObject(urlProductService,Product.class);

        }catch (Exception e){
            throw new CustomException("Có lỗi khi call api tới product service hoặc product không tồn tại");
        }
    }

    public ResponseEntity<?> save(OrderRequestDTO orderRequestDTO) {
        try {
            Customer customer = findCustomerById(orderRequestDTO.getCustomerId());
            Product product = findProductById(orderRequestDTO.getProductId());
            if(customer == null){
                throw new CustomException(" ID khách hàng không tồn tại");
            }
            if(product == null){
                throw new CustomException("ID sản phẩm không tồn tại");
            }
            if (product.getStockQuantity() < orderRequestDTO.getQuantity()){
                throw new CustomException("Số lượng hàng tồn kho không đủ");
            }
            Order order = new Order();
            order.setCustomerId(orderRequestDTO.getCustomerId());
            order.setProductId(orderRequestDTO.getProductId());
            order.setOrderDate(LocalDateTime.now());
            order.setQuantity(orderRequestDTO.getQuantity());
            order.setTotalAmount(product.getPrice()*orderRequestDTO.getQuantity());
            return new ResponseEntity<>(orderRepository.save(order), HttpStatus.CREATED);
        }catch (Exception e){
            ApiResponseError apiResponseError = ApiResponseError
                    .builder()
                    .status(503)
                    .timestamp(LocalDateTime.now())
                    .error("Service Unavailable")
                    .message("Hệ thống quản lý bác sĩ hiện không khả dụng. Vui lòng đặt lịch sau!")
                    .build();
            return new ResponseEntity<>(apiResponseError, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
