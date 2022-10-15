package com.hanh.job;

import com.hanh.domain.habit.Habit;
import com.hanh.domain.habit.HabitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class HabitJobs {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final HabitRepository habitRepository;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job displayHabit(){
        return jobBuilderFactory.get("displayHabit")
                .start(displayHabitStep())
                .build();
    }

    @Bean
    public Job resetWeekCount(){
        return jobBuilderFactory.get("resetWeekCount")
                .start(resetWeekCountStep())
                .build();
    }

    @Bean
    public Step displayHabitStep (){
        return stepBuilderFactory.get("displayHabitStep")
                .<Habit, Habit> chunk(100)
                .reader(displayHabitReader())
                .processor(displayHabitProcessor())
                .writer(habitWriter())
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step resetWeekCountStep (){
        return stepBuilderFactory.get("resetWeekCountStep")
                .<Habit, Habit> chunk(100)
                .reader(resetWeekCountReader())
                .processor(resetWeekCountProcessor())
                .writer(habitWriter())
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Habit> displayHabitReader(){

        // isDisplay가 true 인 Habit 의 목록
        return new JpaPagingItemReaderBuilder<Habit>()
                .name("displayHabitReader")
                .pageSize(1000)
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT h FROM Habit h where h.isDisplay=true")
                .build();

    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Habit> resetWeekCountReader(){

        // weekCount 가 0 이 아닌 habit 의 목록
        return new JpaPagingItemReaderBuilder<Habit>()
                .name("resetWeekCountReader")
                .pageSize(1000)
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT h FROM Habit h where h.weekCount>0")
                .build();

    }

    @Bean
    @StepScope
    public ItemProcessor<Habit,Habit> displayHabitProcessor(){
        return new ItemProcessor<Habit, Habit>() {
            final LocalDate date = LocalDate.now();

            @Override
            public Habit process(Habit habit) throws Exception {
                if (!habit.getDisplayDateList().contains(date)){
                    habit.addDisplayDate(date);
                }
                return habit;
            }
        };
    }

    @Bean
    @StepScope
    public ItemProcessor<Habit,Habit> resetWeekCountProcessor(){
        return new ItemProcessor<Habit, Habit>() {
            @Override
            public Habit process(Habit habit) throws Exception {
                habit.resetWeekCount();
                return habit;
            }

        };
    }

    @Bean
    @StepScope
    public ItemWriter<Habit> habitWriter(){
        return ((List<? extends Habit>habitList) ->
                 habitRepository.saveAll(habitList));
    }


}
