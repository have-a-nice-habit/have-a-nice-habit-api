package hanh.demo.common.scheduler;

import hanh.demo.common.job.HabitJobs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchScheduler {

    private final HabitJobs habitjobs;
    private final JobLauncher jobLauncher;

    @Async
    @Scheduled(cron=" 0 0 0 * * 1 ")
    public void runDisplayHabit(){

        System.out.println("batch 1 쓰레드명 ="+Thread.currentThread().getName());

        Map<String, JobParameter> confMap = new HashMap<>();
        JobParameters jobParameters = new JobParameters(confMap);

        try{
            jobLauncher.run(habitjobs.resetWeekCount(), jobParameters);
        }catch(JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException
        | JobParametersInvalidException | org.springframework.batch.core.repository.JobRestartException e){
            log.error(e.getMessage());
        }
    }

    @Async
    @Scheduled(cron=" 0 0 0 * * * ")
    public void runResetWeekCount(){

        System.out.println("batch 2 ="+Thread.currentThread().getName());

        Map<String, JobParameter> confMap = new HashMap<>();
        JobParameters jobParameters = new JobParameters(confMap);

        try{
            jobLauncher.run(habitjobs.displayHabit(), jobParameters);
        }catch(JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException
               | JobParametersInvalidException | org.springframework.batch.core.repository.JobRestartException e){
            log.error(e.getMessage());
        }
    }

}
