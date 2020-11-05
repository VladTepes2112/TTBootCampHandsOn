package com.victor.hands_on_7.batchConfig;

import com.victor.hands_on_7.Coupon;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    @Bean
    public FlatFileItemReader<Coupon> reader() {
        return new FlatFileItemReaderBuilder<Coupon>()
                .name("CouponItemReader")
                .resource(new ClassPathResource("coupons.csv"))
                .delimited()
                .names(new String[]{"id", "code", "productId", "discount"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Coupon>() {{
                    setTargetType(Coupon.class);
                }})
                .build();
    }

    @Bean
    public CouponItemProcessor processor() {
        return new CouponItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Coupon> writer(DataSource dataSource) {

        return new JdbcBatchItemWriterBuilder<Coupon>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO coupon (id, code, product_id, discount) VALUES (:id, :code, :productId, :discount)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importCouponJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importCouponJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<Coupon> writer) {
        return stepBuilderFactory.get("step1")
                .<Coupon, Coupon> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }
}