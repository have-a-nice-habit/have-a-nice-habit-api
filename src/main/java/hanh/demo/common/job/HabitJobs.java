package hanh.demo.common.job;

import hanh.demo.habit.domain.Habit;
import hanh.demo.habit.repository.HabitRepository;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    // isDisplay가 true 인 경우 모두 displayDateList 에 날짜 추가
    @Bean
    public Job displayHabit(){
        return jobBuilderFactory.get("displayHabit")
                .start(displayHabitStep())
                .build();
    }

    @Bean
    public Step displayHabitStep (){
        return stepBuilderFactory.get("displayHabitStep")
                .<Habit, Habit> chunk(100)
                .reader(displayHabitReader())
                .processor(displayHabitProcessor())
                .writer(displayHabitWriter())
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
    public ItemProcessor<Habit,Habit> displayHabitProcessor(){
        return new ItemProcessor<Habit, Habit>() {
            final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            final LocalDate date = LocalDate.now();

            @Override
            public Habit process(Habit habit) throws Exception {
                if (habit.getIsDisplay() && !habit.getDisplayDateList().contains(date)){

                    habit.addDisplayDate(date);
                }
                return habit;
            }
        };
    }

    @Bean
    @StepScope
    public ItemWriter<Habit> displayHabitWriter(){
        return ((List<? extends Habit>habitList) ->
                 habitRepository.saveAll(habitList));
    }


}
