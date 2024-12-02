// package org.j1p5.api.product.controller;
//
// import static org.mockito.Mockito.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
// import com.fasterxml.jackson.databind.ObjectMapper;
// import java.time.LocalDateTime;
// import java.util.Collections;
// import org.j1p5.api.product.dto.request.ProductCreateRequestDto;
// import org.j1p5.domain.product.dto.ProductInfo;
// import org.j1p5.domain.product.entity.ProductStatus;
// import org.j1p5.domain.product.service.*;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.mock.web.MockMultipartFile;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.test.web.servlet.MockMvc;
//
// @WebMvcTest(ProductController.class)
// class ProductControllerTest {
//
//    @Autowired private MockMvc mockMvc;
//
//    @MockBean private ProductService productService;
//
//    @Autowired private ObjectMapper objectMapper;
//
//    @Test
//    @WithMockUser(username = "test@example.com")
//    void makeProduct_withValidRequest_shouldReturn200() throws Exception {
//        // Mock ProductRequestDto (record 생성자를 통해 초기화)
//        ProductCreateRequestDto requestDto =
//                new ProductCreateRequestDto(
//                        "Sample Title",
//                        "Sample Content",
//                        1000,
//                        null,
//                        37.7749,
//                        127.4194,
//                        "Sample Address",
//                        "Sample Location",
//                        ProductStatus.BIDDING,
//                        LocalDateTime.now().plusDays(1));
//
//        // Mock Multipart file
//        MockMultipartFile mockFile =
//                new MockMultipartFile(
//                        "images",
//                        "test-image.jpg",
//                        MediaType.IMAGE_JPEG_VALUE,
//                        "test image content".getBytes());
//
//        // Mock service behavior
//        doNothing().when(productService).registerProduct(anyLong(), any(ProductInfo.class),
// any());
//
//        mockMvc.perform(
//                        multipart("/api/v1/products")
//                                .file(mockFile)
//                                .param("request", objectMapper.writeValueAsString(requestDto))
//                                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockUser(username = "test@example.com")
//    void makeProduct_withEmptyImages_shouldReturn200() throws Exception {
//        // Mock ProductRequestDto (record 생성자를 통해 초기화)
//        ProductCreateRequestDto requestDto =
//                new ProductCreateRequestDto(
//                        "Sample Title",
//                        "Sample Content",
//                        1000,
//                        null,
//                        37.7749,
//                        127.4194,
//                        "Sample Address",
//                        "Sample Location",
//                        ProductStatus.BIDDING,
//                        LocalDateTime.now().plusDays(1));
//
//        // Mock service behavior
//        doNothing()
//                .when(productService)
//                .registerProduct(anyLong(), any(ProductInfo.class), eq(Collections.emptyList()));
//
//        mockMvc.perform(
//                        multipart("/api/v1/products")
//                                .param("request", objectMapper.writeValueAsString(requestDto))
//                                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockUser(username = "test@example.com")
//    void makeProduct_withoutRequestPart_shouldReturn400() throws Exception {
//        mockMvc.perform(
//                        multipart("/api/v1/products")
//                                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
//                .andExpect(status().isBadRequest());
//    }
// }
