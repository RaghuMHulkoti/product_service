package com.learning.product_service.config;

import com.learning.product_service.core.processor.brand.CreateBrandProcessor;
import com.learning.product_service.core.processor.brand.ListBrandsProcessor;
import com.learning.product_service.core.processor.category.CreateCategoryAttributeProcessor;
import com.learning.product_service.core.processor.category.CreateCategoryProcessor;
import com.learning.product_service.core.processor.category.GetCategoryTreeProcessor;
import com.learning.product_service.core.processor.media.AddProductMediaProcessor;
import com.learning.product_service.core.processor.product.CreateProductProcessor;
import com.learning.product_service.core.processor.product.DeleteProductProcessor;
import com.learning.product_service.core.processor.product.GetProductProcessor;
import com.learning.product_service.core.processor.product.ListProductsProcessor;
import com.learning.product_service.core.processor.product.UpdateProductProcessor;
import com.learning.product_service.core.service.BrandService;
import com.learning.product_service.core.service.CategoryService;
import com.learning.product_service.core.service.OfferService;
import com.learning.product_service.core.service.OutboxService;
import com.learning.product_service.core.service.ProductMediaService;
import com.learning.product_service.core.service.ProductService;
import com.learning.product_service.core.usecase.brand.CreateBrandUsecase;
import com.learning.product_service.core.usecase.brand.ListBrandsUsecase;
import com.learning.product_service.core.usecase.category.CreateCategoryAttributeUsecase;
import com.learning.product_service.core.usecase.category.CreateCategoryUsecase;
import com.learning.product_service.core.usecase.category.GetCategoryTreeUsecase;
import com.learning.product_service.core.usecase.media.AddProductMediaUsecase;
import com.learning.product_service.core.usecase.product.CreateProductUsecase;
import com.learning.product_service.core.usecase.product.DeleteProductUsecase;
import com.learning.product_service.core.usecase.product.GetProductUsecase;
import com.learning.product_service.core.usecase.product.ListProductsUsecase;
import com.learning.product_service.core.usecase.product.UpdateProductUsecase;
import com.learning.product_service.gateway.BrandGateway;
import com.learning.product_service.gateway.BrandGatewayImpl;
import com.learning.product_service.gateway.CategoryAttributeGateway;
import com.learning.product_service.gateway.CategoryAttributeGatewayImpl;
import com.learning.product_service.gateway.CategoryGateway;
import com.learning.product_service.gateway.CategoryGatewayImpl;
import com.learning.product_service.gateway.ProductEntityMapper;
import com.learning.product_service.gateway.ProductGateway;
import com.learning.product_service.gateway.ProductGatewayImpl;
import com.learning.product_service.gateway.ProductMediaGateway;
import com.learning.product_service.gateway.ProductMediaGatewayImpl;
import com.learning.product_service.gateway.ProductOutboxGateway;
import com.learning.product_service.gateway.ProductOutboxGatewayImpl;
import com.learning.product_service.gateway.SupplierOfferGateway;
import com.learning.product_service.gateway.client.SupplierOfferGatewayImpl;
import com.learning.product_service.gateway.repository.BrandRepository;
import com.learning.product_service.gateway.repository.CategoryAttributeRepository;
import com.learning.product_service.gateway.repository.CategoryRepository;
import com.learning.product_service.gateway.repository.ProductMediaRepository;
import com.learning.product_service.gateway.repository.ProductOutboxRepository;
import com.learning.product_service.gateway.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.json.JsonMapper;

import java.time.Duration;

/**
 * Wires the core layers by hand so that processor, use case and service classes stay
 * free of Spring stereotype annotations.
 */
@Configuration
public class AppConfig {

    // =========================================================================
    // Processors
    // =========================================================================

    @Bean
    public CreateProductProcessor createProductProcessor(CreateProductUsecase usecase) {
        return new CreateProductProcessor(usecase);
    }

    @Bean
    public UpdateProductProcessor updateProductProcessor(UpdateProductUsecase usecase) {
        return new UpdateProductProcessor(usecase);
    }

    @Bean
    public DeleteProductProcessor deleteProductProcessor(DeleteProductUsecase usecase) {
        return new DeleteProductProcessor(usecase);
    }

    @Bean
    public GetProductProcessor getProductProcessor(GetProductUsecase usecase) {
        return new GetProductProcessor(usecase);
    }

    @Bean
    public ListProductsProcessor listProductsProcessor(ListProductsUsecase usecase) {
        return new ListProductsProcessor(usecase);
    }

    @Bean
    public CreateCategoryProcessor createCategoryProcessor(CreateCategoryUsecase usecase) {
        return new CreateCategoryProcessor(usecase);
    }

    @Bean
    public GetCategoryTreeProcessor getCategoryTreeProcessor(GetCategoryTreeUsecase usecase) {
        return new GetCategoryTreeProcessor(usecase);
    }

    @Bean
    public CreateCategoryAttributeProcessor createCategoryAttributeProcessor(CreateCategoryAttributeUsecase usecase) {
        return new CreateCategoryAttributeProcessor(usecase);
    }

    @Bean
    public CreateBrandProcessor createBrandProcessor(CreateBrandUsecase usecase) {
        return new CreateBrandProcessor(usecase);
    }

    @Bean
    public ListBrandsProcessor listBrandsProcessor(ListBrandsUsecase usecase) {
        return new ListBrandsProcessor(usecase);
    }

    @Bean
    public AddProductMediaProcessor addProductMediaProcessor(AddProductMediaUsecase usecase) {
        return new AddProductMediaProcessor(usecase);
    }

    // =========================================================================
    // Use cases
    // =========================================================================

    @Bean
    public CreateProductUsecase createProductUsecase(ProductService productService, CategoryService categoryService,
                                                     BrandService brandService, OutboxService outboxService) {
        return new CreateProductUsecase(productService, categoryService, brandService, outboxService);
    }

    @Bean
    public UpdateProductUsecase updateProductUsecase(ProductService productService, CategoryService categoryService,
                                                     BrandService brandService, OutboxService outboxService) {
        return new UpdateProductUsecase(productService, categoryService, brandService, outboxService);
    }

    @Bean
    public DeleteProductUsecase deleteProductUsecase(ProductService productService, OutboxService outboxService) {
        return new DeleteProductUsecase(productService, outboxService);
    }

    @Bean
    public GetProductUsecase getProductUsecase(ProductService productService, CategoryService categoryService,
                                               BrandService brandService, ProductMediaService productMediaService,
                                               OfferService offerService) {
        return new GetProductUsecase(productService, categoryService, brandService, productMediaService, offerService);
    }

    @Bean
    public ListProductsUsecase listProductsUsecase(ProductService productService, CategoryService categoryService) {
        return new ListProductsUsecase(productService, categoryService);
    }

    @Bean
    public CreateCategoryUsecase createCategoryUsecase(CategoryService categoryService) {
        return new CreateCategoryUsecase(categoryService);
    }

    @Bean
    public GetCategoryTreeUsecase getCategoryTreeUsecase(CategoryService categoryService) {
        return new GetCategoryTreeUsecase(categoryService);
    }

    @Bean
    public CreateCategoryAttributeUsecase createCategoryAttributeUsecase(CategoryService categoryService) {
        return new CreateCategoryAttributeUsecase(categoryService);
    }

    @Bean
    public CreateBrandUsecase createBrandUsecase(BrandService brandService) {
        return new CreateBrandUsecase(brandService);
    }

    @Bean
    public ListBrandsUsecase listBrandsUsecase(BrandService brandService) {
        return new ListBrandsUsecase(brandService);
    }

    @Bean
    public AddProductMediaUsecase addProductMediaUsecase(ProductService productService,
                                                         ProductMediaService productMediaService,
                                                         CategoryService categoryService,
                                                         OutboxService outboxService) {
        return new AddProductMediaUsecase(productService, productMediaService, categoryService, outboxService);
    }

    // =========================================================================
    // Services
    // =========================================================================

    @Bean
    public ProductService productService(ProductGateway productGateway) {
        return new ProductService(productGateway);
    }

    @Bean
    public CategoryService categoryService(CategoryGateway categoryGateway, CategoryAttributeGateway categoryAttributeGateway) {
        return new CategoryService(categoryGateway, categoryAttributeGateway);
    }

    @Bean
    public BrandService brandService(BrandGateway brandGateway) {
        return new BrandService(brandGateway);
    }

    @Bean
    public ProductMediaService productMediaService(ProductMediaGateway productMediaGateway) {
        return new ProductMediaService(productMediaGateway);
    }

    @Bean
    public OfferService offerService(SupplierOfferGateway supplierOfferGateway) {
        return new OfferService(supplierOfferGateway);
    }

    @Bean
    public OutboxService outboxService(ProductOutboxGateway productOutboxGateway, JsonMapper jsonMapper) {
        return new OutboxService(productOutboxGateway, jsonMapper);
    }

    // =========================================================================
    // Gateways
    // =========================================================================

    @Bean
    public ProductEntityMapper productEntityMapper() {
        return new ProductEntityMapper();
    }

    @Bean
    public ProductGateway productGateway(ProductRepository repository, ProductEntityMapper mapper) {
        return new ProductGatewayImpl(repository, mapper);
    }

    @Bean
    public CategoryGateway categoryGateway(CategoryRepository repository, ProductEntityMapper mapper) {
        return new CategoryGatewayImpl(repository, mapper);
    }

    @Bean
    public CategoryAttributeGateway categoryAttributeGateway(CategoryAttributeRepository repository, ProductEntityMapper mapper) {
        return new CategoryAttributeGatewayImpl(repository, mapper);
    }

    @Bean
    public BrandGateway brandGateway(BrandRepository repository, ProductEntityMapper mapper) {
        return new BrandGatewayImpl(repository, mapper);
    }

    @Bean
    public ProductMediaGateway productMediaGateway(ProductMediaRepository repository, ProductEntityMapper mapper) {
        return new ProductMediaGatewayImpl(repository, mapper);
    }

    @Bean
    public ProductOutboxGateway productOutboxGateway(ProductOutboxRepository repository) {
        return new ProductOutboxGatewayImpl(repository);
    }

    @Bean
    public SupplierOfferGateway supplierOfferGateway(RestClient supplierRestClient) {
        return new SupplierOfferGatewayImpl(supplierRestClient);
    }

    /** Short timeouts: product detail must not hang when Supplier Service is slow (TDD §8.1, 300 ms default). */
    @Bean
    public RestClient supplierRestClient(
            @Value("${supplier-service.base-url}") String baseUrl,
            @Value("${supplier-service.connect-timeout:300ms}") Duration connectTimeout,
            @Value("${supplier-service.read-timeout:1s}") Duration readTimeout
    ) {
        var requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);
        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .build();
    }
}
