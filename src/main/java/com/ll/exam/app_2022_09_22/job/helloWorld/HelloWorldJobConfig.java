package com.ll.exam.app_2022_09_22.job.helloWorld;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class HelloWorldJobConfig {

    private final JobBuilderFactory jobBuilderFactory; // job을 빌드를 해주는 Factory이다.

    private final StepBuilderFactory stepBuilderFactory; // step을 만드는 객체

    @Bean
    public Job helloWorldJob() { // 잡 생성 메서드
        return jobBuilderFactory.get("helloWorldJob") // "helloWorldJob" 을 이름을 가지는 Job 빌더를 가져오게 해준다. DB에도 helloWorldJob이라고 저장된다.
//                .incrementer(new RunIdIncrementer()) // 강제로 매번 다른 ID를 실행시에 파라미터로 부여
                .start(helloWorldStep1()) // step 을 넣어주는 것이다.
                .next(helloWorldStep2()) // 다음 step 추가.
                .build();
    }

    @JobScope
    @Bean
    public Step helloWorldStep1() { // 스탭 생성 메서드
        return stepBuilderFactory.get("helloWorldStep1")
                .tasklet(helloWorld1Tasklet())// 간단한 것은 tasklet(테스클릿)으로 가능하다. 복잡한것은 item으로 해야한다.
                .build();
    }

    @StepScope
    @Bean
    public Tasklet helloWorld1Tasklet() {
        return (contribution, chunkContext) -> {
            System.out.println("헬로1 월드!");
            return RepeatStatus.FINISHED; // 일이 잘 맞춰줬다는 상태를 알려주는 것이다. ( 작업 상태를 성공, 실팽 등 알려주는 곳 )
        };
    }

    @JobScope
    @Bean
    public Step helloWorldStep2() { // 스탭 생성 메서드
        return stepBuilderFactory.get("helloWorldStep2")
                .tasklet(helloWorld2Tasklet())// 간단한 것은 tasklet(테스클릿)으로 가능하다. 복잡한것은 item으로 해야한다.
                .build();
    }

    @StepScope
    @Bean
    public Tasklet helloWorld2Tasklet() {
        return (contribution, chunkContext) -> {
            System.out.println("헬로2 월드!");
            if ( false ) {
                throw new Exception("실패 : 헬로월드 테스클릿 2");
            }
            return RepeatStatus.FINISHED; // 일이 잘 맞춰줬다는 상태를 알려주는 것이다. ( 작업 상태를 성공, 실팽 등 알려주는 곳 )
        };
    }
}