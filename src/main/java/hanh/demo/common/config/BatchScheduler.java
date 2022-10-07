package hanh.demo.common.config;

import hanh.demo.common.job.HabitJobs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
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

    @Scheduled(cron=" 0 33 0 * * * ")
    public void runJob(){

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
