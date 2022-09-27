package com.ll.exam.app_2022_09_22.job.productBackup;

import com.ll.exam.app_2022_09_22.app.product.entity.Product;
import com.ll.exam.app_2022_09_22.app.product.entity.ProductBackup;
import com.ll.exam.app_2022_09_22.app.product.repository.ProductRepository;
import com.ll.exam.app_2022_09_22.app.product.repository.ProductBackupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Collections;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class ProductBackupJobConfig {
    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    // 주문 품목
    private final ProductRepository productRepository; // 읽을 대상
    // 정산 데이터 품목
    private final ProductBackupRepository productBackupRepository; // 쓰기 대상

    @Bean
    public Job productBackupJob(Step productBackupStep1, CommandLineRunner initData) throws Exception {
        initData.run();

        return jobBuilderFactory.get("productBackupJob")
                .start(productBackupStep1)
                .build();
    }

    @JobScope
    @Bean
    public Step productBackupStep1(
            ItemReader productReader,
            ItemProcessor productProcessor,
            ItemWriter productWriter) {

        return stepBuilderFactory.get("productBackupStep1")
                // OrderItem, RebatedOrderItem을 받는다.
                // chunk(1)한번에 하나씩 읽어온다.
                .<Product, ProductBackup>chunk(1)
                .reader(productReader)
                .processor(productProcessor)
                .writer(productWriter)
                .build();
    }


    @StepScope
    @Bean
    // RepositoryItemReader는 Repository에서 읽어오는 것이다.
    public RepositoryItemReader<Product> productReader() {

        return new RepositoryItemReaderBuilder<Product>()
                .name("productReader")
                .repository(productRepository)
                // orderItemRepository.findAll() 을 실행
                .methodName("findAll")
                // pageSize 하나씩 읽어옴
                .pageSize(1)
                //파라미터 받아오기
                .arguments(Arrays.asList())
                // orderBy 해주기.
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }
    @StepScope
    @Bean
    public ItemProcessor<Product, ProductBackup> productProcessor() {
        // product 를 productBackup으로 바꿔주는 용도.
        return product -> new ProductBackup(product);
    }

    @StepScope
    @Bean
    public ItemWriter<ProductBackup> productWriter() {
        return items -> items.forEach(item -> {
                ProductBackup oldProductBackup = productBackupRepository.findByProductId(item.getProduct().getId()).orElse(null);
                if ( oldProductBackup != null ) {
                    productBackupRepository.delete(oldProductBackup);
                }
                productBackupRepository.save(item);
        });
    }
}
